//0&1&2&3&4&5&termineWhile&6&exitosamente
class A extends System{
    static void main() {
        var a = 0;
        while(a <= 5) {
            printIln(a);
            a += 1;
        }
        printSln("termineWhile");
        printIln(a); //6
    }
}