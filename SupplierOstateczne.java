
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;

public class Supplier implements SupplierInterface{

    final HashMap<Integer, Integer> inventory = new HashMap<>();

    @Override
    public void add(int objectID, int quantity) {
        synchronized (inventory) {
            if(inventory.containsKey(objectID) == false)
                inventory.put(objectID, quantity);
            else {
                int old = inventory.get(objectID);
                inventory.put(objectID, old+quantity);
            }
            inventory.notifyAll();
        }
    }

    @Override
    public void request(Map<Integer, Integer> order) {
        if (order.isEmpty()){
            return;
        }

        while (true) {
            synchronized (inventory) {
                LinkedList<Integer> zamowienie = new LinkedList<>();
                boolean requestReady = false;

                for (Map.Entry<Integer, Integer> entry : order.entrySet()) {
                    int key = entry.getKey();
                    int value = entry.getValue();

                    if (!inventory.containsKey(key))
                        break;
                    if (inventory.get(key) >= value){
                        zamowienie.add(1);
                    }else break;
                    if (zamowienie.size() == order.size()){
                        requestReady = true;
                    }
                    if (requestReady){
                        for (Map.Entry<Integer, Integer> gotowe : order.entrySet()){
                            int klucz = gotowe.getKey();
                            int wartosc = gotowe.getValue();
                            int poprzedniaWartosc = inventory.get(klucz);
                            
                            inventory.put(klucz, poprzedniaWartosc - wartosc);
                            
                            if (inventory.get(klucz) == 0) 
                                inventory.remove(klucz);
                        }
                        return;
                    }
                }
                    try {
                        inventory.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    @Override
    public Map<Integer, Integer> getInventory() {
        synchronized (inventory){
            return inventory;
        }
    }
}
