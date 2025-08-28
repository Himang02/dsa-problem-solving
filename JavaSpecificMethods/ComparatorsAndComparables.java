import java.util.*;

/* Comparable Interface
    * Used for defining natural ordering of objects of a class
    * Class must implement Comparable interface and override compareTo() method
    * compareTo() method returns negative, zero, positive value if this object is less than, equal to, greater than the specified object
    * Collections.sort() uses compareTo() method for sorting
    * Example: String, Integer, Double etc. classes implement Comparable interface
    * If class does not implement Comparable, then ClassCastException is thrown at runtime
    * If class implements Comparable, then we can use Collections.sort() or Arrays.sort() to sort the objects of that class
*/
/* Comparator Interface
    * Used for defining custom ordering of objects of a class
    * Create a separate class that implements Comparator interface and override compare() method
    * compare() method returns negative, zero, positive value if first argument is less than, equal to, greater than the second argument
    * Collections.sort() and Arrays.sort() have overloaded versions that accept Comparator as argument
    * Example: We can create a Comparator to sort strings by length, or to sort students by name etc.
    * If class implements Comparable, then we can still use Comparator to override natural ordering
    * If class does not implement Comparable, then we must use Comparator to sort the objects of that class
    * If no Comparator is provided for a class that does not implement Comparable, then ClassCastException is thrown at runtime
    * We can use lambda expressions to create Comparator instances in a concise way
    * We can use Comparator.comparing() and thenComparing() methods to create complex Comparators
    * We can use Comparator.reverseOrder() to get a Comparator that imposes the reverse of the natural ordering
    * We can use Comparator.reversed() to get a Comparator that imposes the reverse of the ordering imposed by another Comparator
    * We can use Comparator.nullsFirst() and Comparator.nullsLast() to handle null values
*/
public class ComparatorsAndComparables {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        List<Student> list = new ArrayList<>();
        list.add(new Student(3, "John"));
        list.add(new Student(1, "Alice"));
        list.add(new Student(2, "Bob"));

        Collections.sort(list); // uses compareTo
        System.out.println(list);
    
    }
}


class Student implements Comparable<Student> {
    int id;
    String name;

    Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Natural ordering: by id
    @Override
    public int compareTo(Student other) {
        return this.id - other.id; // ascending order by id
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}

