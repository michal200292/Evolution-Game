package org.project.MapObjects;

import java.util.Comparator;

public class AnimalComparator implements Comparator<Animal> {

    @Override
    public int compare(Animal o1, Animal o2) {
        if(o1 == o2){
            return 0;
        }
        if(o1.energy != o2.energy){
            return o1.energy - o2.energy;
        }
        if(o1.age != o2.age){
            return o1.age - o2.age;
        }
        if(o1.numberOfChildren != o2.numberOfChildren){
            return o1.numberOfChildren - o2.numberOfChildren;
        }
        return 0;
    }
}
