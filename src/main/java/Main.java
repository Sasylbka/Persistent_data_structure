import PersistentDataStructure.PersistentArray;
import PersistentDataStructure.PersistentLinkedList;
import PersistentDataStructure.PersistentTreeMap;

public class Main {
    public static void main(String [] args){
        System.out.println("Проверка на изменяемость при добавлении элемента в массив");
        PersistentArray <Integer> array= new PersistentArray<>(1);
        array.add(0);
        System.out.println("Тот же массив после add:"+array);
        PersistentArray <Integer> arrayVersion2=array.add(0);
        System.out.println("Новый массив после add:"+arrayVersion2);

        System.out.println("\nПроверка на изменяемость при изменении элемента в массиве");
        arrayVersion2.set(0,1);
        System.out.println("Тот же массив после применения set:"+arrayVersion2);
        PersistentArray <Integer> arrayVersion3=array.set(0,1);
        System.out.println("Новый массив, полученный после set:"+arrayVersion3);

        System.out.println("\nПроверка на изменяемость при удалении последнего элемента в массиве");
        arrayVersion2.pop();
        System.out.println("Тот же массив после pop:"+arrayVersion2);
        PersistentArray <Integer> arrayVersion4=arrayVersion2.pop();
        System.out.println("Новый массив после pop:"+arrayVersion4);

        System.out.println("\nПроверка на переход по версиям назад");
        PersistentArray <Integer> arrayVersion5=arrayVersion2.add(10);
        System.out.println("Массив 5ой версии:"+arrayVersion5);
        PersistentArray<Integer> tempArray=arrayVersion5.undo();
        System.out.println("Прошлая версия массива 5ой версии:"+tempArray);
        System.out.println("Которая равна 2ой версии:"+arrayVersion2);

        System.out.println("\nПроверка на переход по версиям вперёд");
        System.out.println("Массив второй версии:"+tempArray);
        tempArray=tempArray.redo();
        System.out.println("Будущая версия массива 2ой версии:"+tempArray);
        System.out.println("Которая равна 5ой версии:"+arrayVersion5);

        System.out.println("\nПереход из массива в список");
        PersistentLinkedList<Integer> newList=arrayVersion5.toPersistentLinkedList();
        System.out.println("Список по массиву 5ой версии:"+newList);

        System.out.println("\nПроверка на изменяемость списка после add");
        PersistentLinkedList<String> empty=new PersistentLinkedList<>(1);
        PersistentLinkedList<String> listVersion1=empty.add(0,"test");
        System.out.println("изначальный список:"+listVersion1);
        listVersion1.add(1,"test2");
        System.out.println("Тот же список после add:"+listVersion1);
        PersistentLinkedList<String> listVersion2=listVersion1.add(1,"test2");
        System.out.println("изменённая версия:"+listVersion2);

        System.out.println("\nПроверка на изменяемость списка после remove");
        System.out.println("изначальный список:"+listVersion2);
        listVersion2.remove(0);
        System.out.println("Тот же список после remove:"+listVersion2);
        PersistentLinkedList<String> listVersion3=listVersion2.remove(0);
        System.out.println("изменённая версия:"+listVersion3);

        System.out.println("\nПроверка на переход по версиям назад");
        PersistentLinkedList<String> listVersion4=listVersion3.undo();
        System.out.println("Прошлая версия 3ей версии списка:"+listVersion4);
        System.out.println("Которая равна 2ой версии списка"+listVersion2);

        System.out.println("\nПроверка на переход по версиям вперёд");
        PersistentLinkedList<String> listVersion5=listVersion4.redo();
        System.out.println("Будующая версия 2ой версии списка:"+listVersion5);
        System.out.println("Которая равна 3ей версии списка"+listVersion3);

        System.out.println("\nПереход из списка в массив");
        PersistentArray <String> tempArray2=listVersion1.toPersistentArray();
        System.out.println(tempArray2);

        System.out.println("\nПроверка на изменяемость мапы после put");
        PersistentTreeMap<Integer,String> emptyMap=new PersistentTreeMap<>(1);
        System.out.println("изначальный мап:"+emptyMap);
        emptyMap.put(99999,"test");
        System.out.println("Тот же мап после put:"+emptyMap);
        PersistentTreeMap<Integer,String> mapVersion1=emptyMap.put(99999,"test");
        System.out.println("изменённая версия:"+mapVersion1);

        System.out.println("\nПроверка на изменяемость мапы после remove");
        System.out.println("изначальный мап:"+mapVersion1);
        mapVersion1.remove(99999);
        System.out.println("Тот же мап после remove:"+mapVersion1);
        PersistentTreeMap<Integer,String> mapVersion2=mapVersion1.remove(99999);
        System.out.println("изменённая версия:"+mapVersion2);

        System.out.println("\nПроверка на переход по версиям назад");
        PersistentTreeMap<Integer,String> mapVersion3=mapVersion2.undo();
        System.out.println("Прошлая версия 3ей версии списка:"+mapVersion3);
        System.out.println("Которая равна 2ой версии списка"+mapVersion1);

        System.out.println("\nПроверка на переход по версиям вперёд");
        PersistentTreeMap<Integer,String> mapVersion4=mapVersion3.redo();
        System.out.println("Прошлая версия 3ей версии списка:"+mapVersion4);
        System.out.println("Которая равна 2ой версии списка"+mapVersion2);






    }
}
