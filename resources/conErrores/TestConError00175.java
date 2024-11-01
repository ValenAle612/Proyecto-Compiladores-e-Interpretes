///[Error:==|14]

class A {

    public B v1;
    private A v2;
    private C v3;

     void m1()
    {
       v1 = null;
       if(v1 == v2) {
           if(v2 == v1) {
               if(v3 == v1){
                   if(v3 == v2) {

                   }
               }
           }
       }
    }

}

class B extends A {}

class C extends A{}


class Init{
    static void main()
    { }
}

