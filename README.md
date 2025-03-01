**关于优雅国际化的具体实现**

添加 aop，无感国际化，只需要在范围类配置注解，以及字段添加类型

环绕 AOP

目前方案 start

目前支持阶段小语种方式的优雅查询，大 json 还在计划

1. orgIdField（org_id对应对象中的字段名称）
2. localeField（语言环境对应对象中的字段名称）
3. enable（是否开启国际化）
4. interfaceType（接口类型）
5. fallback（是否启用兜底策略（当找不到对应语言资源时，回退到默认语言））


1. tableName（表名）
2. type（国际化类型，一般一个业务表对应一个type）
3. bizIdField（业务id对应对象中的字段名称）
4. disabled（默认查询有效  0：有效 1：无效 -1：查询全部）

目前方案 end



```

```

添加缓存字段信息✅
性能提高近 14.8倍

1. 第1个测试 查询单个标准              405 ms -> 20 ms
2. 第2个测试 查询多个标准              427 ms -> 30 ms
3. 第3个测试 查询单个不标准            393 ms -> 32 ms
4. 第4个测试 查询多个不标准            394 ms -> 30 ms
5. 第5个测试 查询多个不标准 Result 标准类 470 ms -> 29 ms



集合优化，批量查询国际化信息，减少sql查询次数✅

测试多个注解，已经支持返回类多个国际化注解✅

1. 第1个测试 查询单个标准                                                     11ms
2. 第2个测试 查询多个标准                                                     13 ms
3. 第3个测试 查询单个不标准                                                 11 ms
4. 第4个测试 查询多个不标准                                                 13 ms
5. 第5个测试 查询多个不标准 Result 标准类                          12 ms
6. 第6个测试 查询多个不标准多个国际化字段 Result 标准类 14 ms

todo list



| 功能点                                 | 优先级 |
| -------------------------------------- | ------ |
| 兜底                                   | 1✅     |
| 对象嵌套                               | 3      |
| 分页查询                               | 2✅     |
| 国际化信息加缓存（后端视角：远程缓存） | 2      |
| 多种国际化策略                         | 2      |
| 多兜底语言方案                         | 3      |
| 多兜底方案                             | 4      |



最后理想形态：**业务无侵入、本地缓存（字段）➕远程缓存（国际化信息）➕查库、多国际化策略、支持对象嵌套、多兜底方案**

目前进度：业务无侵入、本地缓存（字段）➕查库、兜底

小疑问：前端如果做了 CDN 后端还需要做国际化了

- **必须做**：后端仍需实现国际化，因为 CDN 仅能处理静态资源，而动态内容、用户偏好、格式适配等必须由后端处理。

- **协作模式**：前端 CDN 负责静态资源的分发和缓存，后端负责动态内容的国际化，两者通过 HTTP 头（如 `Accept-Language`）或 URL 参数传递语言上下文。

**最终目标**：通过前后端协作，实现**全链路国际化**，覆盖从静态资源到动态内容的所有场景。



```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface I18n {

    /**
     * org_id对应对象中的字段名称
     * @return
     */
    String orgIdField() default "orgId";

    /**
     * 语言环境对应对象中的字段名称
     * @return
     */
    String localeField() default "locale";

//    /**
//     * 默认查询有效  0：有效 1：无效 -1：查询全部
//     * @return
//     */
//    int status() default 0;

    /**
     * 是否开启国际化
     * @return
     */
    boolean enable() default true;

    /**
     * 接口类型
     * @return
     */
    InterfaceTypeEnums interfaceType() default InterfaceTypeEnums.OUTER;

    /**
     * 是否启用兜底策略（当找不到对应语言资源时，回退到默认语言）
     * @return
     */
    boolean fallback() default false;
}
```







**先实现查表、单个对象的国际化处理**

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface PowerI18n {

    /**
     * 表名
     * @return
     */
    I18nTableEnums tableName() default I18nTableEnums.INTERNATIONAL;

    /**
     * 国际化类型，一般一个业务表对应一个type
     * @return
     */
    I18nTypeEnums type();

//    /**
//     * org_id对应对象中的字段名称
//     * @return
//     */
//    String orgIdField() default "orgId";

    /**
     * 业务id对应对象中的字段名称
     * @return
     */
    String bizIdField();

    /**
     * 默认查询有效  0：有效 1：无效 -1：查询全部
     * @return
     */
    int disabled() default 0;

//    /**
//     * 是否值查询一个语种 一条数据
//     * @return
//     */
//    boolean isSingle() default true;

}
```





```java
/**
 * @ClassName I18nAspect
 * @Description todo 写在前面
 * 本质也就是去查表，无非是关系型数据库和非关系型数据库，把这个操作在方法返回时做出来
 * 是去数据库找到数据对应语言环境的
 * 国际化处理切面类，用于处理带有@I18n注解的方法返回结果的字段国际化替换
 * @Author power
 * @Date 2025/2/26 1:23
 * @Version 1.0
 */
@Component
@Aspect
@Slf4j
public class I18nAspect {

    @Autowired
    private I18nService i18nService;

    @Autowired
    private OrganizationService organizationService;

    // 缓存类字段信息，避免重复反射
    /**
     * 每一个类的所有字段映射关系
     */
    private static final ConcurrentHashMap<Class<?>, Map<String, Field>> classFieldCache = new ConcurrentHashMap<>();
    /**
     * 每一个类所有需要国际化的字段
     */
    private static final ConcurrentHashMap<Class<?>, Map<Field, PowerI18n>> powerI18nFieldsCache = new ConcurrentHashMap<>();


//    final String SALT = "this is my salt";
//    final int MIN_HASH_LENGTH = 6;
//
//    Hashids hashids = new Hashids(SALT, MIN_HASH_LENGTH);

    /**
     * 定义切点，拦截所有被@I18n注解标记的方法
     */
    @Pointcut("@annotation(com.power.annotation.I18n)")
    public void pt() {
    }

    /**
     * 环绕通知，处理国际化逻辑
     * @param joinPoint 连接点对象，用于获取方法执行上下文
     * @return 处理后的方法返回值
     * @throws Throwable 可能抛出的异常
     */
    @Around("pt()")
    public Object i18nHandleContent(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = joinPoint.proceed();

        I18n methodI18n = getMethodI18n(joinPoint);
        // 国际化处理
        if (!methodI18n.enable()) {
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            log.info("{} 国际化未启用~~~", method.getName());
            return proceed;
        }

        Object arg = joinPoint.getArgs()[0];

        String orgId = JSONUtil.parseObj(arg).getStr(methodI18n.orgIdField());
        String locale = JSONUtil.parseObj(arg).getStr(methodI18n.localeField());
        boolean isFallback = false;
        if (InterfaceTypeEnums.OUTER.equals(methodI18n.interfaceType())) {
            isFallback = true;
        } else if (InterfaceTypeEnums.INNER.equals(methodI18n.interfaceType())) {
            isFallback = methodI18n.fallback();
        }

        this.extractFieldValue(proceed, orgId, locale, isFallback);

        return proceed;
    }


    /**
     * 提取并设置单个对象的国际化字段值
     * @param proceed 目标方法返回的对象
     * @param orgId 组织ID
     * @param locale 当前语言环境
     * @param isFallback 是否启用回退逻辑
     */
    @SneakyThrows
    private void extractFieldValue(Object proceed, String orgId, String locale, boolean isFallback) {
        // todo 这里如果解析错误，做解析适应自己项目的类就行

        // 可自定义标准返回类
        if (proceed instanceof Result) {
            proceed = ((Result<?>) proceed).getData();
        }

        if (proceed instanceof Collection) {
            // todo 待优化 isFallback
            this.extractBatchFieldValue((Collection<?>) proceed, orgId, locale, isFallback);
            return;
        }
        // todo Page -> proceed = (Page) proceed.getRecords()

        // todo 默认支持简单的 单个对象，集合(List)  暂不支持嵌套
        // 获取所有字段(包括私有字段)
        Class<?> clazz = proceed.getClass();
        this.executeClassField(clazz);

        Map<String, Field> fieldMap = classFieldCache.getOrDefault(clazz, new LinkedHashMap<>());

        Map<Field, PowerI18n> hasPowerI18nMap = powerI18nFieldsCache.getOrDefault(clazz, new LinkedHashMap<>());

        List<String> localeList = CollUtil.newArrayList(locale);
        if (isFallback) {
            localeList.add(organizationService.getMainLocaleByOrgId(orgId));
            localeList = ListUtils.removeDuplicatesAndFilterNulls(localeList);
        }

        for (Map.Entry<Field, PowerI18n> fieldPowerI18nEntry : hasPowerI18nMap.entrySet()) {
            Field field = fieldPowerI18nEntry.getKey();
            PowerI18n powerI18n = fieldPowerI18nEntry.getValue();

            String tableName = powerI18n.tableName().getTableName();
            Integer type = powerI18n.type().getType();
            int disabled = powerI18n.disabled();

            Field bizIdFiled = fieldMap.get(powerI18n.bizIdField());
            Long bizId = (Long) bizIdFiled.get(proceed);

            Map<String, String> contentMap = i18nService.getContent(tableName, orgId, bizId, type, localeList, disabled);
            String content = this.getContentFallback(orgId, localeList, bizId, type, contentMap);
            field.set(proceed, content);
        }
    }

    /**
     * 批量处理集合类型对象的国际化字段值
     * @param proceedCollection 目标方法返回的对象集合
     * @param orgId 组织ID
     * @param locale 当前语言环境
     * @param isFallback 是否启用回退逻辑
     */
    @SneakyThrows
    private void extractBatchFieldValue(Collection<?> proceedCollection, String orgId, String locale, boolean isFallback) {
        if (CollUtil.isEmpty(proceedCollection)) {
            return;
        }
        List<?> list = CollUtil.newArrayList(proceedCollection);
        Class<?> clazz = list.get(0).getClass();
        this.executeClassField(clazz);
        Map<String, Field> fieldMap = classFieldCache.getOrDefault(clazz, new LinkedHashMap<>());

        // 一个类有多个带这个注解的字段（todo 注意目前支持一层）
        Map<Field, PowerI18n> hasPowerI18nMap = powerI18nFieldsCache.getOrDefault(clazz, new LinkedHashMap<>());

        List<String> localeList = CollUtil.newArrayList(locale);
        if (isFallback) {
            localeList.add(organizationService.getMainLocaleByOrgId(orgId));
            localeList = ListUtils.removeDuplicatesAndFilterNulls(localeList);
        }

        for (Map.Entry<Field, PowerI18n> fieldPowerI18nEntry : hasPowerI18nMap.entrySet()) {
            Field field = fieldPowerI18nEntry.getKey();
            PowerI18n powerI18n = fieldPowerI18nEntry.getValue();

            // 现在获取了字段的信息，和一些注解里的死值  ====================================================================================================
            String tableName = powerI18n.tableName().getTableName();
            Integer type = powerI18n.type().getType();
            int disabled = powerI18n.disabled();

            Field bizIdFiled = fieldMap.get(powerI18n.bizIdField());

            // 获取变值
            List<Long> bizIdList = CollUtil.newArrayList();

            for (Object object : list) {
                Long bizId = (Long) bizIdFiled.get(object);
                bizIdList.add(bizId);
            }
            //
            Map<String, String> contentMap = i18nService.getContent(tableName, orgId, bizIdList, type, localeList, disabled);

            for (Object object : list) {
                Long bizId = (Long) bizIdFiled.get(object);
                String content = this.getContentFallback(orgId, localeList, bizId, type, contentMap);
                field.set(object, content);
            }
        }

    }

    /**
     * 根据语言环境列表获取字段内容的回退值
     * @param orgId 组织ID
     * @param localeList 语言环境优先级列表
     * @param bizId 业务ID
     * @param type 国际化类型
     * @param contentMap 国际化内容缓存
     * @return 匹配到的国际化内容
     */
    private String getContentFallback(String orgId, List<String> localeList, Long bizId, Integer type, Map<String, String> contentMap) {
        String content = null;

        for (String localeItem : localeList) {
            String key = orgId + bizId + type + localeItem;
            content = contentMap.get(key);
            if (content != null) {
                break;
            }
        }
        return content;
    }

    /**
     * 解析并缓存类的字段信息（包含@PowerI18n注解处理）
     * @param clazz 要处理的类类型
     */
    private void executeClassField(Class<?> clazz) {
        // 获取所有字段(包括私有字段)

        Map<String, Field> fieldMap = classFieldCache.getOrDefault(clazz, new LinkedHashMap<>());

        Map<Field, PowerI18n> hasPowerI18nMap = powerI18nFieldsCache.getOrDefault(clazz, new LinkedHashMap<>());

        if (MapUtil.isNotEmpty(fieldMap) && MapUtil.isNotEmpty(hasPowerI18nMap)) {
            return;
        }

        List<String> bizIdFieldStrList = CollUtil.newArrayList();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            fieldMap.put(field.getName(), field);
            PowerI18n powerI18n = field.getAnnotation(PowerI18n.class);
            if (powerI18n != null) {
                // 破坏注解的字段的封装性
                field.setAccessible(true);
                hasPowerI18nMap.put(field, powerI18n);

                bizIdFieldStrList.add(powerI18n.bizIdField());
            }
        }
        // 破坏注解中 bizId 的字段的封装性
        for (String bizIdFiledStr : bizIdFieldStrList) {
            fieldMap.get(bizIdFiledStr).setAccessible(true);
        }

        classFieldCache.put(clazz, fieldMap);
        powerI18nFieldsCache.put(clazz, hasPowerI18nMap);
    }

    /**
     * 获取方法上的@I18n注解实例
     * @param joinPoint 连接点对象
     * @return 方法上的I18n注解实例
     */
    private I18n getMethodI18n(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        I18n annotation = methodSignature.getMethod().getAnnotation(I18n.class);
        return annotation;
    }


    //    @Around("pt()")
//    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
//        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = requestAttributes.getRequest();
//
//        PowerI18n classPowerI18n = getClassPowerI18n(joinPoint);
//        PowerI18n methodPowerI18n = getMethodPowerI18n(joinPoint);
//
//        Object proceed = joinPoint.proceed();
//
//        // 先处理单个的
//        // 处理单个数据和List
//
//        // 国际化处理
//        if (!classPowerI18n.enable()) {
//            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
//            log.info("{} 国际化未启用~~~", method.getName());
//            return proceed;
//        }
//
//        // sdk？
//        String locale = "zh_CN";
//
//        // 注意这里处理的是出参
//        String tableName = classPowerI18n.tableName().getTableName();
//
//        String orgIdField = classPowerI18n.orgIdField();
//        String orgId = request.getParameter(orgIdField); // todo
//        //
//        String bizIdField = classPowerI18n.bizIdField();
//
//        Long bizId = Long.valueOf(request.getParameter(bizIdField));
//        String localeField = classPowerI18n.localeField();
//        I18nTypeEnums typeField = classPowerI18n.type();
//        int status = classPowerI18n.status();
//
//        i18nMapper.queryItemContent(tableName, orgId, bizId, typeField.getType(), locale);
//        // todo spel表达式，controller + response class
//        // todo 字段维度的 两个注解 1.用于 aop spel locale 2.具体的信息   暂时计划只支持 单个和 List (分页在思考) ，复杂嵌套目前还没有考虑
//
//

//        return proceed;
//    }
}
```









后置 



注解可以用在类和字段上
String tablePrefix
Integer type

aop 逻辑：
切点是使用这个注解的类

具体逻辑切面锁定到这个类，然后遍历字段（包含父类字段）中的使用这个类的字段，





以及接口调用的优化。



todo start

国际化类型（表存储国际化信息，字段大json）

表存储国际化信息
1.表前缀
2.表名字
3.业务字段 id 名称（本类中字段名）
4.要转为语言字段名称（本类中字段名）
5.默认语言（默认主语言）

字段大json（前提需要规定 json 国际化信息的格式）
1.大 json 字段名称（本类中字段名）
2.要转为语言字段名称（本类中字段名）
3.默认语言（默认主语言）

todo end

是否需要兜底 true false

兜底规则，一般 用户输入 -> 主语言 -> 中文
