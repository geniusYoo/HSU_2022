# Android STUDY

강의 번호: Android

- Kotlin Basic
    - Packages
        - 자바와 다르게 코틀린은 소스파일 위치와 이름 정하는데 제한 X
        - 한 파일에 여러개 클래스 O, 패키지구조 = 디렉토리구조 굳이 일치할 필요 X
        - main함수가 독립적으로 존재
        - 세미콜론 없어도 됨
    - Variables & Value
        - val
            - val로 선언된 변수는 변경 불가
            - 변수라고 안부르고 → “값” 이라고 부름
            - value
            - `val a: Int = 10`
                
                `~~a = 11 ← compile error~~`
                
        - var
            - var로 선언된 변수는 변경 가능
            - variable
            - `var b: Int = 12`
                
                `b = 13 ← ok`
                
    - Type Inference, 타입 추론
        - 타입 명시할 필요 X, 컴파일러가 알아서 타입을 정해줌
        - `val integerVal : Int = 10`
            
            → `val integerVal = 10` 
            
    - Types
        - primitive type (원시 타입)과 wrapper type 구분 X
        - 명시적인 값 변환 필요
        - `val i2 : UInt = 10U`
            
            `~~val l : Long = i ← compile error~~`
            
            `val l2 : Long = i.toLong()`
            
    - If 문
        - statement가 아니라 expression으로 사용 가능 → 즉, if문의 결과로 어떤 값을 받을 수 있음
        - `val x = if (a > 10) “big” else “small”` → if는 big이나 small 을 리턴
    - is, as
        - is
            - `if (obj is Int) println(”obj is Int!”)` → obj가 Int타입이면, print
        - as
            - `obj as? String` → obj가 String이면 ok, 아니면 NULL
            - `obj as? String ?: “ ”` → as의 결과가 NULL이면 빈 문자열 “ ” 출력 (Elvis 연산자)
    - Any
        - `val t : Any = 10`
            
            `println( (t is Int) ) → ok`
            
    - String
        
        ```kotlin
        val inner = "Kotlin"
        val String = “Hello, This is My ${inner}” // Hello, This is My Kotlin
         println (””” 
            |hello              // 이 문자열들 자체가 객체가 됨
            |my name is Kotlin.
            “””.trimMargin()) // trimMargin() : 객체의 메소드로 공백제거 메소드 사용
        ```
        
    - 비교 연산 (== or ===)
        - 내용 비교는 ==
        - 같은 객체를 가리키는가 ? 즉, 레퍼런스 비교는 ===
    - 배열
        
        ```kotlin
        val intArrays = arrayOf(1, 2, 3) // 초기값 세팅해서 생성
        val strArrays = arrayOfNulls<String>(5) // 초기값 null로 생성
        val dbArrays = emptyArray<Double>() // 비어있는 배열 생성, 초기화X
        
        for(s in strArrays) {
        	print("$s, ") // null, null ... 5개 출력됨
        }
        ```
        
        - val로 정의된 배열이여도, 배열 내의 값은 변경 가능
    - for and Iteration
        
        ```kotlin
        val array = arrayOf("a", "b", "c", "d", "e")
        for (a in array)
        	print("$a ") // a b c d e
        
        for ((idx,a) in array.withIndex())
        	print("($idx,$a) ") // (1,a) (2,b) (3,c) ..
        
        for (i in 1..10)
        	print("$i ") // 1 2 3 4 5 6 7 8 9 10
        
        for(i in 'a'..'f')
        	print("$i ") // a b c d e f
        
        for(i in 1..10 step 2)
        	print("$i ") // 1 3 5 7 9
        
        (10 downTo 1).forEach {
        	print("$it ") // 10 9 8 7 6 5 4 3 2 1
        }
        
        var i=0
        while(i<10) {
        	i++
        	print("$i ") // 1 2 3 4 5 6 7 8 9 10
        }
        ```
        
    - Function
        - `fun 함수이름 (매개변수 리스트) : 리턴타입 { 함수 정의 }`
        - `fun 함수이름 (매개변수 리스트) = expression` → 함수 내용이 expression이면 리턴타입이 추론 가능하므로 생략
        
        ```kotlin
        //1
        fun myFunc(arg1:Int, arg2:String="default", arg3:Int=10)
        {
        	println("$arg1, $arg2, $arg3")
        }
        
        //2 함수 내용이 expression, a+b는 무조건 Int
        fun sumFunc(a:Int, b:Int) = a + b
        
        //3 매개변수 Int, 리턴타입 Int
        fun localFunc(a:Int) : Int {
        	return a + 10
        }
        ```
        
    - **Function - Lambda**
        - Lambda Function = 익명 함수
        - {인자 → 함수 내용}
        - 인자가 1개인 경우 디폴트 인자 **it** 을 써서 생략 가능
        
        ```kotlin
        data class MyClass (val a:Int, val b:String)
        
        fun lambdaTest(a : (Int) -> Int) : Int {
        	return a(10)
        }
        // lambdaTest는 함수 a를 인자로 받는 함수 
        // a 는 Int를 인자로 받아 Int로 리턴하는 함수
        // lamdbaTest는 a를 인자로 받아 Int로 리턴하는 함수
        
        val sum = { x:Int, y:Int -> x+y}
        println(sum(10,20))
        
        val array = arrayOf(MyClass(10,"class1"), MyClass(20,"class2"), 
        MyClass(30,"class3"))
        // filter 함수를 인자로 받아서 true 되는것만 모아서 리턴하는 함수
        println(array.filter({ c:MyClass -> c.a < 15})) //[MyClass(a=10, b=class1)]
        // 람다가 함수의 인자의 마지막으로 사용되면 함수 바깥으로 뺄 수 있음
        array.filter(){ c:MyClass -> c.a < 15}
        // 람다가 함수의 유일한 인자이면 함수의 괄호 생략 가능
        array.filter { c:MyClass -> c.a < 15}
        // 인자 타입 생략 가능한 경우
        array.filter { c -> c.a < 15 }
        // 디폴트 매개변수 it 사용
        array.filter { it.a < 15 }
        
        println(lambdaTest {it + 10}) // {it+10}은 람다함수, lambdaTest에 함수를 인자로 삽입
        ```
        
        ```kotlin
        // Android Programming에서 많이 쓰는 Lambda Function Example
        val btn = findViewById<Button>(R.id.button1)!!
        btn.setOnClickListener({
        	Toast.makeText( ... )
        })
        ```
        
    - When
        - switch와 비슷
        - 케이스마다 타입이 달라도 됨
        - expression으로도 사용가능
        
        ```kotlin
        fun test (arg:Any) {
        	when(arg) {
        		10 -> println("10")
        		in 0..9 -> println("0<=x<=9")
        		is String -> println("Hello, $")
        		!in 0..10 -> println("range off")
        		else -> { println("unknown") }
        	}
        }
        
        fun test2 (arg : Int) : Int {
        	return when(arg) {
        		in 0..9 -> 10
        		in 10..19 -> 20
        		in 20..29 -> 30
        	}
        }
        ```
        
    - Null Safety
        - null이 가능한 타입과 불가능한 타입 구분
        - 타입 이름 뒤에 ? 붙이면 nullable 타입
        
        ```kotlin
        import java.lang.NullPointerException
        
        fun testNull(arg : String?) {
        	println(arg?.toUpperCase()) // arg가 null이면 arg?.toUpperCase() 통째로 null
        	println(arg?.toUpperCase() ?: "-")
        }
        
        var x: String? = null // nullable
        var y: String = "Hello" // nonNullable
        // y = null -> compile error
        
        testNull(x) // null -
        testNull(y) // HELLO HELLO
        
        // x.toUpperCase -> compile error, nullable 가능하다고 ? 붙여줘야함
        x?.toUpperCase()
        try {
        	x!!.toUpperCase() // x는 nullable이지만 null이 되었을때 exception 걸어줘!
        } catch (e : NullPointerException) {
        	println("NullPointerException") 
        }
        ```
        
    - Exception
        
        ```kotlin
        import java.lang.NumberFormatException
        import java.lang.Integer.parseInt
        
        val x = try {
        	parseInt("10")
        } catch (e : NumberFormatException) {
        	0 // 10으로 변환 안되면 x에 0을 넣어줌
        }
        ```
        
    - Collections
        - Immutable : Array, List, Set, Map → 원소의 개수를 바꿀 수 없음
        - Mutable : ArrayList, MutableList, MutableSet, MutableSet, MutableMap → 가능
        
        ```kotlin
        val array = arrayOf(1, 2, 3)
        val arrayList = array.toMutableList() // arrayListOf(1,2,3)
         
        val list = listOf(1, 2, 3)
        val mutableList = list.toMutableList() // mutableListof(1,2,3)
        
        val set = setOf(1, 2, 3, 3) // 중복 제외
        val mutableSet = set.toMutableSet() // mutableSetOf(1,2,3,3)
        
        val map = mapOf("one" to 1, "two" to 2, "three" to 3)
        val mutableMap = map.toMutableMap() // mutableMapOf(...)
        
        arrayList.add(4)
        arrayList.[arrayList.lastIndex]++
        
        mutableList.add(4)
        mutableList[0] = 0
        
        mutableSet.add(4)
        
        mutableMap["four"] = 4
        
        println(arrayList) // [1,2,3,5]
        println(mutableList) // [0,2,3,4]
        println(mutableSet) // [1,2,3,4]
        println(mutableMap) // [one=1, two=2, three=3, four=4]
        
        ```
        
- Object Oriented Programming In Kotlin
    - Class
        - `class 클래스이름 (생성자 인자 리스트)`
        - 생성자 인자들 중 val, var가 붙은 것은 클래스의 속성이 됨
        - 기본적으로 public class
        - new keyword X
        
        ```kotlin
        class Animal(val name: String)
        
        class Person(val firstName:String, _lastName:String) {
        	var lastName : String = _lastName
        		get() = field.uppercase()
        		set(value) {
        			field = "[$value]"
        		}
        }
        
        val a = Animal("Dog")
        println("Animal : ${a.name}") // Animal : Dog
        
        val p = Person("Youngjae", "You")
        println("Name : ${p.firstName} ${p.lastName}") // Name : Youngjae You
        p.lastName = "YOU"
        println("Name : ${p.firstName} ${p.lastName}") // Name : Youngjae YOU
        ```
        
    - Interface
        - override 꼭 써야함
        
        ```kotlin
        interface ClickListener {
        	fun onClick()
        	fun onTouch() = println("touched")
        }
        
        class View(private val id : Int) : ClickListener {
        	override fun onClick() = println("clicked $id")
        }
        
        val v = View(10)
        v.onClick()
        v.onTouch()
        ```
        
    - 기본적으로 상속, override X
        - interface, abstract class는 가능
        - 기본적으로 class는 상속, 메소드 오버라이딩 금지 → 가능하게 하려면 open 키워드 사용
        - 접근 지정자
            - final : 오버라이드 금지, 기본적으로 final
            - open : 오버라이드 가능, 명시적으로 open 붙여야 오버라이드 가능
            - abstract : 반드시 오버라이드 해야 함, abstract class 내에서만 사용 가능
            - override : 오버라이드 가능
    - 기본적으로 public
        - 클래스와 메소드 모두 기본적으로 public
        - 접근 변경자
            - public : 기본 접근
            - internal : 같은 모듈 (gradle의 모듈과 같이 같이 컴파일되는 단위)
            - protected : 상속 받은 클래스에서만 접근 가능
            - private : 같은 클래스에서만 접근 가능
    - 중첩 클래스, Nested Class
        - 자바의 static 중첩 클래스와 같음
        - 자바처럼 내부 클래스 만드려면 중첩 클래스 앞에 inner 키워드 사용
        
        ```kotlin
        class Outer(var m : Int = 0) {
        	inner class Inner {
        		fun doSomething() {
        			this@Outer.m++
        			println("Inner doSomething ${this@Outer.m}")
        		}
        	}
        	
        	class NoInner { // inner 키워드 안써서 중첩 클래스로 적용 안됨
        		fun doSomething() {
        			println("No Inner doSomething")
        		}
        	}
        }
        
        val inner = Outer().Inner()
        inner.doSomething()
        
        val noInner = Outer().NoInner() // 오류, inner class 아님!
        noInner.doSomething()
        ```
        
    - 봉인 클래스, Sealed Class
        - 특정 클래스를 상속하는 클래스를 제한
        
        ```kotlin
        sealed class Expr {
        	class Num(val value: Int) : Expr
        	class Sum(val left: Expr, val right: Expr) : Expr()
        }
        
        fun eval(e : Expr): Int = 
        	when(e) {
        		is Expr.Num -> e.value
        		is Expr.Sum -> eval(e.right) + eval(e.left)
        	}
        
        val r = eval(Expr.Sum(Expr.Num(10), Expr.Num(10)))
        println(r) // 20
        ```
        
    - Constructor
        - primary constructor : 클래스 이름 옆에 정의하는 생성자
        - secondary constructor : 클래스 내부에 정의하는 생성자
        
        ```kotlin
        class ConstEx1 constructor(_prop : Int) { // primary 
        	val prop : Int
        	val prop2 : Int
        	init {
        		prop = _prop
        		prop2 = _prop*2
        	}
        }
        
        open class ConstEx2 (val prop:Int, val prop2: Int=0) { // secondary 
        	constructor(_prop : Int, _prop2: Int, _prop3 : Int) : 
        		this(_prop + _prop3, _prop2){ .... }
        }
        ```
        
    - Property & Getter/Setter
        - Property의 custom getter, setter
        
        ```kotlin
        interface GetSetI {
        	val prop : String
        }
        
        class GetSet(_prop : String) : GetSetI {
        	override val prop : String = _prop
        		set(value) {
        			field = value.substringbefore('@')
        		}
        		get() = field.uppercase()
        
        	var prop2 :Int = 0
        		private set
        }
        ```
        
    - Data Class
        - equals, hashCode, toString, Copy 메소드들이 자동으로 생성
        - `data class MyClass(val a: Int, val b: String)`
    - Object 키워드
        - 클래스 정의 없이 바로 객체 생성
        - 싱글톤, companion object, anonymous object 만들 때 사용
        
        ```kotlin
        object ClickListenerImp1 : ClickListener {
        	override fun onClick() = println("Checked")
        }
        
        fun setClickListener(listener: CLickListener) = listener.onClick()
        
        class Touch() {
        	val objectNums : Int
        		get() = num
        
        	init{
        		num++
        	}
        
        	companion object {
        		var num : Int = 0
        	}
        }
        
        setClickListener(ClickListenerImp1)
        setClickListener(object : ClickListener {
        	override fun onClick() = println("Checked2")
        })
        Touch()
        Touch()
        println(Touch().objectNums()) // 3
        ```
        
    - Extenstion Method, Property
        - 이미 만들어진 클래스의 메소드와 속성을 상속,수정 안하고 추가
        - 이렇게 추가된 메소드는 오버라이드 불가
        - 자바의 기존 Collection들에 다양한 유틸 함수들을 추가
        
        ```kotlin
        val String.lastChar: Char
        	get() = get(length -1)
        
        println("Hello".lastChar)
        ```
        
    - Delegation, 위임
        - 상속과 유사하지만, 다름
        - 상속은 강한 결합, 위임은 약한 결합
        - 상속 불가여도 위임은 가능
        - 위임할 클래스와 동일한 인터페이스를 구현해야 함
        - 위임 패턴은 위임할 객체를 멤버로 만들고 특정 메소드 호출 시 위임 객체의 메소드로 호출하는 방식
        - by 키워드 사용
        
        ```kotlin
        interface Base {
        	fun print()
        	fun printHello()
        }
        
        class BaseImp1(val x : Int) : Base {
        	override fun print() { print(x) }
        	override fun printHello() { println("Hello") }
        {
        
        class Derived(val b : Base) : Base by b {
        	override fun print() {
        		b.print()
        		println("ok")
        	}
        }
        
        val b = BaseImp1(10)
        val d = Derived(b)
        d.print() // Derived 클래스에서 오버라이드 한 것을 사용
        d.printHello() // Derived에서 구현하지 않았지만 사용 가능
        ```
        
    - Delegation Property, 위임 속성
        - by 키워드 사용해서 속성의 get, set → 위임 객체의 getValue, setValue
        - 특성
            - Lazy : 값을 처음 접근할 때 생성, 계산
            - Observable : 값이 변경될 때 알려주는 리스너를 지정
            - Map을 이용해 객체 속성 값을 저장
    - Generic
    
- Functional Programming In Kotlin
    - 함수형 프로그래밍
        - 함수형 코드 : 인수 x에 같은 값을 넣고 함수 f를 호출하면 항상 f(x)라는 결과가 나옴
    - Lambda
        - { x:Int, y:Int → x + y }
        - 함수 body에서 마지막 수식(expression)이 리턴 값이 됨
    - Collection - filter, map, groupBy
        - Collection은 언어 문법 X, 표준 라이브러리 O
        - filter : 특정 조건을 만족하는 원소만 포함하는 collection을 생성, 리턴
            - filter에 주어진 람다를 모든 원소에 수행하여 true를 리턴하는 경우만 모음
            - filter에 넘겨주는 람다는 boolean을 결과로 하는 수식이어야 함 → predicate
        - map : 모든 원소에 대해 특정 연산을 수행한 결과를 모아서 collection을 생성, 리턴
            - map에 주어진 람다를 모든 원소에 대해 수행하고 그 결과를 모음
        - groupBy : 주어진 조건에 따라 collection을 그룹으로 나눈 후 map을 생성, 리턴
            - 이 때, map은 key와 value를 가진 것으로 위의 메소드 map과는 다름!
    - Collection - all, any, count, find
        - 고차함수임, 함수를 인자로 받기 때문 !
        - all : 모든 원소가 특정 조건을 만족하면 true, 그렇지 않으면 false
        - any : 한 원소라도 특정 조건을 만족하면 true, 그렇지 않으면 false
        - count : 특정 조건을 만족하는 원소의 개수 리턴
        - find : 특정 조건을 만족하는 가장 처음 원소를 리턴
        
        ```kotlin
        val nums = arrayOf(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5)
        
        println(nums.all { it is Int }) // true
        println(nums.any { it > 0 }) // true
        println(nums.count { it > 0 }) // 5
        println(nums.find { it > 0 })	// 1
        ```
        
    - Collection - flatMap
        - 중첩된 collection을 하나의 리스트로 생성, 리턴
        - flatMap에 주어진 람다는 Iterable 객체를 리턴
            - 이 Iterable을 모두 연결해 하나의 리스트로 만듦
            - `public inline fun <T, R> Iterable<T>.flatMap(transform: (T) → Iterable<R> ) : List<R>`
        
        ```kotlin
        val list = listOf("abc", "cde", "efg")
        
        println(list.flatMap { it.toList() }) // [a, b, c, c, d, e, e ... ]
        println(list.flatMap { it.toList() }.toSet()) // [a, b, c, d, e, f, g]
        
        class MyClass(val name: String, val students: List<String>)
        val classes = listOf(MyClass("a", listOf("a1", "a2", "a3")), 
        										 MyClass("b", listOf("b1", "b2", "b3")))
        
        println(classes.flatMap {it.students}.toSet().sorted())
        
        // sorted : abc 순으로 정렬
        // toSet : 동일한 원소 제거
        // "abc".toList() -> ['a', 'b', 'c']
        ```
        
    - Collection - asSequence
        - filter, map 등의 함수를 부르면 다른 리스트가 바로 생김 → 매우 큰 데이터 여러번,, 매우 느려짐
        - Sequence : 리스트 생성을 최대한 늦춤, lazy 연산이라고 부름
        - Sequence를 사용해 연산 끝내고 다시 toList()로 Collection을 바꿔서 사용함
    - SAM, Single Abstract Method
        - 인터페이스가 하나의 메소드만 가진 경우
        - 오버라이드 하지 않고 람다로 처리
    - Scope 함수
        - 객체의 이름을 반복하지 않고, 그 객체에 대해 여러 연산을 수행
        - 함수 인자로 lambda를 전달하는데, 이 람다 내에서 객체를 it 또는 this로 지칭
        - 함수의 리턴값은 객체 자체 or 람다의 결과
        - Scope 함수는 언어 문법 X, 표준 라이브러리 O