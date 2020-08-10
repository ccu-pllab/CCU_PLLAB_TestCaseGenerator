package ccu.pllab.tcgen.libs;

 
import java.util.Map;

public interface Predicate {

	public abstract String getPredicateName(Map<String, String> templateArgs);

	public abstract String getEntirePredicate(Map<String, String> templateArgs);

}