///[Error:this|8]
//

class A {
    private int a1;

    static void m1(int p1){
        m2(this.a1);
    }

    static void m2(int p2) {}

}



class Init{
    static void main()
    { }
}