//a: &5&b: &5&exitosamente

class Init{
    static void main(){
        var a = 0;
        var b = 0;
        while( a < 5 ){
            b += 1;
            a = b;
        }
        System.printS("a: ");
        System.printIln(a);
        System.printS("b: ");
        System.printIln(b);
    }
}

class A{

}
