class ReturnTest {
    public static void main() {
        var example = new ReturnExample();
        var result = example.calculate(5);  // Llamada válida
    }
}

class ReturnExample {
    public int calculate(int x) {
        if (x > 0) {
            return x * 2;
        } else if (x < 0) {
            return x / 2;
        }
        // Sin return en caso de x == 0, lo que debería provocar un error
    }
}
