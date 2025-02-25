package test;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Sneaker sneaker1 = new Sneaker("nike","1",10.99);
        Sneaker sneaker2 = new Sneaker("addidas","2",15.99);
        Sneaker sneaker3 = new Sneaker("addidas","3",20.99);
        Sneaker sneaker4 = new Sneaker("nike","7",30.99);
        Sneaker sneaker5 = new Sneaker("converse","5",39.99);
        Sneaker sneaker6 = new Sneaker("addidas","6",40.99);

        HashMap<String,Sneaker> map = new HashMap<>();
        map.put("sneaker1",sneaker1);
        map.put("sneaker2",sneaker2);
        map.put("sneaker3",sneaker3);
        map.put("sneaker4",sneaker4);
        map.put("sneaker5",sneaker5);
        map.put("sneaker6",sneaker6);

        /*TreeSet<Map.Entry<String, Integer>> entrySet = new TreeSet<>(
                Comparator.comparing(Map.Entry::getKey)
        );*/
        TreeSet<Sneaker> sneakerSet = new TreeSet<>(Comparator
                .comparing(Sneaker::getBrand)
                .thenComparing(Comparator.comparing(Sneaker::getPrice).reversed()));  // Ordenar per preu (descendent)

        sneakerSet.addAll(map.values());

        for (Sneaker sneaker : sneakerSet) {
            System.out.println(sneaker);
        }

        //orderSneaker(map);


    }

    /*private static void orderSneaker(HashMap<String, Sneaker> map) {
        Scanner sc = new Scanner(System.in);
        System.out.println("¿Te gustaría ordenar por marca (A>Z) o modelo (A>Z)?");
        String option = sc.nextLine();

        List<Sneaker> sneakers = new ArrayList<>(map.values());

        if (option.equalsIgnoreCase("marca")) {
            sneakers.sort(Comparator.comparing(Sneaker::getBrand));
        }else if (option.equalsIgnoreCase("modelo")) {
            sneakers.sort(Comparator.comparing(Sneaker::getModel));
        } else{
            System.out.println("opcion invalida");
            return;
        }
        for (Sneaker sneaker : sneakers) {
            System.out.println(sneaker);
        }

    }*/
}

