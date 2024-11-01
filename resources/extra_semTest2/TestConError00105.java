///[Error:a2|21]
//El atributo a2 no se puede acceder con B porque es un atributo privado de A
class A {
    public B a1;
    private int a2;
    
    A m1(int p1) {

       
    }
    
    B m2() {}
         
    

}


class B extends A{
    void m3() {
        this.m1(1).m2().a1.a2 = 567;
    }
}


class Init{
    static void main()
    { }
}


