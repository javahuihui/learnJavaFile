# java8 Optional静态类简介，以及用法

在java8中，很多的stream的终端操作，都返回了一个Optional<T>对象，这个对象，是用来解决空指针的问题，而产生的一个类；我们先看下，这个类的一些定义

```jaa
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
 
public final class Optional<T> {
	private static final Optional<?> EMPTY = new Optional<>();
 
	private final T value;
 
	private Optional() {
		this.value = null;
	}
 
	public static <T> Optional<T> empty() {
		@SuppressWarnings("unchecked")
		Optional<T> t = (Optional<T>) EMPTY;
		return t;
	}
 
	private Optional(T value) {
		this.value = Objects.requireNonNull(value);
	}
 
	public static <T> Optional<T> of(T value) {
		return new Optional<>(value);
	}
 
	public static <T> Optional<T> ofNullable(T value) {
		return value == null ? empty() : of(value);
	}
 
	public T get() {
		if (value == null) {
			throw new NoSuchElementException("No value present");
		}
		return value;
	}
 
	public boolean isPresent() {
		return value != null;
	}
 
	public void ifPresent(Consumer<? super T> consumer) {
		if (value != null)
			consumer.accept(value);
	}
 
	public Optional<T> filter(Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		if (!isPresent())
			return this;
		else
			return predicate.test(value) ? this : empty();
	}
 
	public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
		Objects.requireNonNull(mapper);
		if (!isPresent())
			return empty();
		else {
			return Optional.ofNullable(mapper.apply(value));
		}
	}
 
	public <U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper) {
		Objects.requireNonNull(mapper);
		if (!isPresent())
			return empty();
		else {
			return Objects.requireNonNull(mapper.apply(value));
		}
	}
 
	public T orElse(T other) {
		return value != null ? value : other;
	}
 
	public T orElseGet(Supplier<? extends T> other) {
		return value != null ? value : other.get();
	}
 
	public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
		if (value != null) {
			return value;
		} else {
			throw exceptionSupplier.get();
		}
	}
 
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
 
		if (!(obj instanceof Optional)) {
			return false;
		}
 
		Optional<?> other = (Optional<?>) obj;
		return Objects.equals(value, other.value);
	}
 
	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
 
	@Override
	public String toString() {
		return value != null ? String.format("Optional[%s]", value) : "Optional.empty";
	}
}
```

上面这些定义，我把java的源代码，去掉了注释，所展示的部分，我们可以看下，这个类，有两个构造方法，以及三个静态方法

如下

```java
	private Optional() {
		this.value = null;
	}
 
	private Optional(T value) {
		this.value = Objects.requireNonNull(value);
	}
 
	public static <T> Optional<T> empty() {
		@SuppressWarnings("unchecked")
		Optional<T> t = (Optional<T>) EMPTY;
		return t;
	}
 
	public static <T> Optional<T> of(T value) {
		return new Optional<>(value);
	}
 
	public static <T> Optional<T> ofNullable(T value) {
		return value == null ? empty() : of(value);
```

构造方法被私有化，外部不能直接创建这个对象，静态方法中，也提供了实例化这个类的三个静态方法，

## 第一个是empty方法

直接返回一个类加载后就创建的一个空的optional对象，

## 第二个是of(T value)方法

可以看到，直接new了一个optional对象；

## 第三个是ofNullable(T value)方法

可以看到，这个方法，先对传入的泛型对象，做了null的判断，为null的话，返回第一个静态方法的空对象；

创建完optional对象后，我们来看下，取到这个泛型对象的几个方法

```java
public T get() {
		if (value == null) {
			throw new NoSuchElementException("No value present");
		}
		return value;
	}
 
	public T orElse(T other) {
		return value != null ? value : other;
	}
 
	public T orElseGet(Supplier<? extends T> other) {
		return value != null ? value : other.get();
	}
 
	public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
		if (value != null) {
			return value;
		} else {
			throw exceptionSupplier.get();
		}
	}
```

可以看到，有4种取泛型参数的方式；

1.get()直接取，如果为null，就返回异常

2.orElse(T other)在取这个对象的时候，设置一个默认对象（默认值）；如果当前对象为null的时候，就返回默认对象

3.orElseGet(Supplier<? extends T> other)跟第二个是一样的，区别只是参数，传入了一个函数式参数； 

4.orElseThrow(Supplier<? extends X> exceptionSupplier)第四个，跟上面表达的是一样的，为null的时候，返回一个特定的异常；

下面，我们再看下，剩下的几个方法

```java
public boolean isPresent() {
		return value != null;
	}
 
	public void ifPresent(Consumer<? super T> consumer) {
		if (value != null)
			consumer.accept(value);
	}
 
	public Optional<T> filter(Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		if (!isPresent())
			return this;
		else
			return predicate.test(value) ? this : empty();
	}
 
	public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
		Objects.requireNonNull(mapper);
		if (!isPresent())
			return empty();
		else {
			return Optional.ofNullable(mapper.apply(value));
		}
	}
 
	public <U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper) {
		Objects.requireNonNull(mapper);
		if (!isPresent())
			return empty();
		else {
			return Objects.requireNonNull(mapper.apply(value));
		}
	}
```



isPresent()是对当前的value进行null判断

ifPresent(Consumer<? super T> consumer)具体的意思，可以参看《JAVA8 Consumer接口》，理解了consumer接口，也就理解了这个方法；

filter(Predicate<? super T> predicate)，map(Function<? super T, ? extends U> mapper) ，flatMap(Function<? super T, Optional<U>> mapper) 这几个方法的应用，可以参看我的《JAVA8 Stream接口，map操作，filter操作，flatMap操作》，跟stream的上的方法，用法是一致的，没有区别；



## 案例分析

最后，我们用几个案例，来演示一下，这个类的使用方式，大家自己观看代码，就不做解释了

```java
import java.util.Arrays;
 
public class Test {
 
	public static void main(String[] args) {
		Emp emp = new Emp("xiaoMing", "上海", "11");
		Optional<Emp> op = Optional.ofNullable(emp);
		System.out.println(op.get().getAddress());// 上海
		Optional<Emp> op1 = Optional.ofNullable(null);
		System.out.println(op1.orElse(emp).getAddress());// 上海
		/*
		 * 这里指定了一个默认对象emp，为先创建的一个emp对象，emp对象里的成员变量还没有复制，所以输出为null
		 */
		System.out.println(op1.orElseGet(Emp::new).getAddress());
		try {
			System.out.println(op1.orElseThrow(RuntimeException::new));// java.lang.RuntimeException
 
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			System.out.println(op1.get().getAddress());// java.util.NoSuchElementException
		} catch (Exception e) {
			e.printStackTrace();
		}
		String address = op.filter(obj -> obj.getAddress().equals("上海")).map(str -> str.getAddress()).get();
		System.out.println(address);// 上海
 
	}
 
	static class Emp {
		private String name;
 
		private String address;
 
		private String age;
 
		public Emp() {
			super();
		}
 
		public Emp(String name, String address, String age) {
			super();
			this.name = name;
			this.address = address;
			this.age = age;
		}
 
		public String getName() {
			return name;
		}
 
		public void setName(String name) {
			this.name = name;
		}
 
		public String getAddress() {
			return address;
		}
 
		public void setAddress(String address) {
			this.address = address;
		}
 
		public String getAge() {
			return age;
		}
 
		public void setAge(String age) {
			this.age = age;
		}
 
	}
}
```

