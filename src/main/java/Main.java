class Main {

    public static void main(String[] args) {
        System.out.println("principal: " + args[0]);
        System.out.println("keytab: " + args[1]);
        System.out.println("url: " + args[2]);

        KerberosSample ks = new KerberosSample(args[0], args[1], args[2]);
        ks.run();

        return;
    }
}
