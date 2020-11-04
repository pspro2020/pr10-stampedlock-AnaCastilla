import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.StampedLock;

public class Almacen {


    private Random rnd = new Random();
    private ArrayList<Producto>  products = new ArrayList<>();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final StampedLock stampedLock = new StampedLock();


    public Almacen () {
        generateList();
    }

    public ArrayList<Producto> generateList() {
        int num;
        for (int i = 0; i < 5; i++) { //La lista tendrá 5 productos al principio
            num = rnd.nextInt(3)+1;
            Producto newProduct = new Producto(num);
            products.add(newProduct);
        }

        return products;
    }


    public int getStock(int id) {
        long stamp = stampedLock.readLock();
        try {
            return consultStock(id);
        } finally {
            stampedLock.unlock(stamp);
        }
    }

    public void addProduct(int id) {
        long stamp = stampedLock.writeLock();

        try {
            products.add(new Producto(id));
        } finally {
            stampedLock.unlock(stamp);
        }
    }

    public int consultStock(int id) {
        int cont = 0;

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == id) {
                cont++;
            }
        }

        System.out.printf("%s - Stock of Product %s: %d\n",
                LocalTime.now().format(dateTimeFormatter), id, cont);
        return cont;
    }


}
