//[Error:a1|19]

class A {
    private int a1;
    
    
     void m1(){
        a1 = a1;
    }
    

}


class B extends A{

    private int a2;
    void m1(){
        this.a1 = 6;
        this.a2 = 56;
    }
    
}


class Init{
    static void main()
    { }
}


