# springboot启动时执行：CommandLineRunner、ApplicationRunner、ApplicationListener @PostConstruct 对比



#### 使用场景：

我们在开发过程中会有这样的场景：需要在项目启动后执行一些操作，比如：读取配置文件信息，数据库连接，删除临时文件，清除缓存信息，工厂类初始化，加载活动数据，或者缓存的同步等。我们会有多种的实现方式，例如@PostConstruct 、CommandLineRunner、ApplicationRunner、ApplicationListener都可以实现在springboot启动后执行我们特定的逻辑，接下对比下他们的区别

# 1 @PostConstruct

该注解被用来修饰一个非静态的void方法，被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次。

###### 触发时机：

SpringBoot会把标记了Bean相关注解（例如@Component、@Service、@Repository等）的类或接口自动初始化全局的单一实例，如果标记了初始化顺序会按照用户标记的顺序，否则按照默认顺序初始化。在初始化的过程中，执行完一个Bean的构造方法后会执行该Bean的@PostConstruct方法（如果有），然后初始化下一个Bean。
 spring中bean的创建过程

> 配置Bean(@Component、@Service、@Controller等注解配置) -----> 解析为Bean的元数据(Bean容器中的BeanDefinition对象) --> 根据Bean的元数据生成Bean（创建bean）

创建bean的时候执行顺序

> Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的方法)

###### 示例：



```java
@PostConstruct
public void dispatcher() throws Exception {
    // 逻辑代码
}
```

###### 优点：

- 使用简单，在spring容器管理的类中添加此注解即可

###### 缺点：

- .在spring创建bean的时候触发，此时容器还未完全初始化完毕，如果逻辑中引用了还未完成初始化的bean会导致异常 ，所以需要考虑加载顺序。
- 如果@PostConstruct方法内的逻辑处理时间较长，就会增加SpringBoot应用初始化Bean的时间，进而增加应用启动的时间。因为只有在Bean初始化完成后，SpringBoot应用才会打开端口提供服务，所以在此之前，应用不可访问。
- 一句话：会影响你程序启动的时间。

# 2 、CommandLineRunner、ApplicationRunner

使用起来很简单，只需要实现CommandLineRunner或者ApplicationRunner接口，重写run方法就行。

###### 触发时机：

通过springboot启动源码：

启动后会执行      callRunners方法;



```kotlin
public ConfigurableApplicationContext run(String... args) {
   StopWatch stopWatch = new StopWatch();
   //设置线程启动计时器
   stopWatch.start();
   ConfigurableApplicationContext context = null;
   Collection<SpringBootExceptionReporter> exceptionReporters = new ArrayList<>();
   //配置系统属性：默认缺失外部显示屏等允许启动
   configureHeadlessProperty();
   //获取并启动事件监听器，如果项目中没有其他监听器，则默认只有EventPublishingRunListener
   SpringApplicationRunListeners listeners = getRunListeners(args);
   //将事件广播给listeners
   listeners.starting();
   try {
       //对于实现ApplicationRunner接口，用户设置ApplicationArguments参数进行封装
      ApplicationArguments applicationArguments = new DefaultApplicationArguments(
            args);
      //配置运行环境：例如激活应用***.yml配置文件      
      ConfigurableEnvironment environment = prepareEnvironment(listeners,
            applicationArguments);
      configureIgnoreBeanInfo(environment);
      //加载配置的banner(gif,txt...)，即控制台图样
      Banner printedBanner = printBanner(environment);
      //创建上下文对象，并实例化
      context = createApplicationContext();
      exceptionReporters = getSpringFactoriesInstances(
            SpringBootExceptionReporter.class,
            new Class[] { ConfigurableApplicationContext.class }, context);
      //配置SPring容器      
      prepareContext(context, environment, listeners, applicationArguments,
            printedBanner);
      //刷新Spring上下文，创建bean过程中      
      refreshContext(context);
      //空方法，子类实现
      afterRefresh(context, applicationArguments);
      //停止计时器：计算线程启动共用时间
      stopWatch.stop();
      if (this.logStartupInfo) {
         new StartupInfoLogger(this.mainApplicationClass)
               .logStarted(getApplicationLog(), stopWatch);
      }
      //停止事件监听器
      listeners.started(context);
      //开始加载资源
      callRunners(context, applicationArguments);
   }
   catch (Throwable ex) {
      handleRunFailure(context, listeners, exceptionReporters, ex);
      throw new IllegalStateException(ex);
   }
   listeners.running(context);
   return context;
}
```

callRunners方法：

```java
private void callRunners(ApplicationContext context, ApplicationArguments args) {
    //将实现ApplicationRunner和CommandLineRunner接口的类，存储到集合中
   List<Object> runners = new ArrayList<>();
   runners.addAll(context.getBeansOfType(ApplicationRunner.class).values());
   runners.addAll(context.getBeansOfType(CommandLineRunner.class).values());
   //按照加载先后顺序排序
   AnnotationAwareOrderComparator.sort(runners);
   for (Object runner : new LinkedHashSet<>(runners)) {
      if (runner instanceof ApplicationRunner) {
         callRunner((ApplicationRunner) runner, args);
      }
      if (runner instanceof CommandLineRunner) {
         callRunner((CommandLineRunner) runner, args);
      }
   }
}
```

从上面源码可以看到 ，在springboot完全初始化完毕后，会执行CommandLineRunner和ApplicationRunner，两者唯一的区别是参数不同，但是不会影响，都可以获取到执行参数。

##### 示例



```java
/**
 * @author
 * @date 2021-08-23 16:19
 */
@Component
public class ServerDispatcher implements CommandLineRunner {
    @Override
    public void run(String... args){
        // 逻辑代码
    }
}
```



```java
/**
 * @author
 * @date 2021-08-23 16:19
 */
@Component
public class ServerDispatcher2 implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args){
        // 逻辑代码
    }
}
```

# 3、ApplicationListener

通过事件监听我们也可以实现springboot启动执行方法。实现ApplicationListener<ContextRefreshedEvent>，重写onApplicationEvent方法，便可在所有的bean加载完毕后执行。

###### 触发时机：

在IOC的容器的启动过程，当所有的bean都已经处理完成之后，spring ioc容器会有一个发布ContextRefreshedEvent事件的动作。

##### 示例



```java
/**
 * @author
 * @date 2021-08-23 16:19
 */
@Component
public class ServerDispatcher3 implements ApplicationListener<ContextRefreshedEvent> {
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // 逻辑代码
    }
}
```

##### 注意：

系统会存在两个容器，一个是root application context ,另一个就是我们自己的 projectName-servlet context（作为root application context的子容器）

- ApplicationContext
  - context.pushevent()
- WebtapplicationContext
  - context.pushevent()



 这种情况下，就会造成onApplicationEvent方法被执行两次。为了避免上面提到的问题，我们可以只在root application context初始化完成后调用逻辑代码，其他的容器的初始化完成，则不做任何处理



```csharp
  //root application context 没有parent
 if (event.getApplicationContext().getParent() == null) { 
    //逻辑代码
  }
```

# 总结

1. 一些比较独立、内容小巧的初始化逻辑，不影响springboot启动速度的使用@PostConstruct注解；
2. 若想通过ApplicationListener事件监听的方式，则需要处理好指定的容器。
3. 在数据初始化层面，不推荐@PostConstruct和ApplicationListener，原因是两者都会影响程序的启动。如果执行逻辑耗时很长，会启动服务就很长。
4. ==本人建议使用 CommandLineRunner、ApplicationRunner的方式，不会影响服务的启动速度 ，处理起来也比较简单。==



