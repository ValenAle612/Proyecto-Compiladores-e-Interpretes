///[Error:m2|10]
//
class A {
    public int a1;
    public int v1;
    

     void m1(int p1) 
    {
        m2(p1+(v1-10), "hola", new A());
    }
    
     void m2(int p1, String x, C p2)
    {}
         
   

}

class B extends A{
}

class C extends B {}


class Init{
    static void main()
    { }
}


