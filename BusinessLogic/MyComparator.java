package BusinessLogic;
import DataModel.Employee;
import java.util.Comparator;

public class MyComparator implements Comparator<Employee> {

    public MyComparator(){}

    @Override
    public int compare(Employee one,Employee two) {
        if(one.getWorkDuration()>two.getWorkDuration())
            return 1;
        else if(one.getWorkDuration()<two.getWorkDuration())
            return -1;
        else {
            return one.getName().compareTo(two.getName());
        }
    }
}
