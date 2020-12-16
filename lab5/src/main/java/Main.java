import injectClasses.Interface1;
import injectClasses.Interface2;

public class Main {
    static class MyClass{
        @AutoInjectable
        private Interface1 field1;

        @AutoInjectable
        private Interface2 field2;

        public void doSomething(){
            field1.sayFromInterface1();
            field2.sayFromInterface2();
        }
    }

    public static void main(String[] args) {
        MyClass obj = Injector.inject(new MyClass());
        obj.doSomething();
    }
}
