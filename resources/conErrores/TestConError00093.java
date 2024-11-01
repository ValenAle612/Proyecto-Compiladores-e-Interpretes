///[Error:=|21]
// No son tipos conformantes
class A {
    public int a1;
    
    void m1(int p1) {

       
    }
    
     void m2()
    {}
         
    

}


class B extends A{
    void m3() {
        this.a1 = true;
    }
}


class Init{
    static void main()
    { }
}


