package memento.tnxrollback;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Solution {
	/*	{
		"user":{
			"name":"alice"
		}
		"customer":{
			"name":"alice",
			"age": 26
		}
	}*/

	/*	{
		"user":{
			"name":"alice"
		}
	}*/


	Map<String, Map<String, String>> store=new HashMap<>();
	Stack<Map<String, Map<String, String>>> tnxStack=new Stack<>();

	public void saveRow(String row){
		store.put(row,new HashMap<>());
	}
	public void updateCol(String row,String filed,String value){
		Map<String,String> rowMap=store.getOrDefault(row,new HashMap<>());
		rowMap.put(filed,value);
		store.put(row,rowMap);
	}
	public void delete(String row){
		store.remove(row);
	}

	public void display(){
		System.out.println("-----------tables print start-----------");
		for (Map.Entry<String,Map<String,String>> mp:store.entrySet()) {
			System.out.println(mp.getKey());
			for (Map.Entry<String, String> m : mp.getValue().entrySet()){
				System.out.println(" " + m.getKey() + " "+m.getValue()+" ");
			}
		}
		System.out.println("-----------tables print end-----------");
	}

	public void begin(){
		Map<String,Map<String,String>> deepCopy=new HashMap<>();
		for (Map.Entry<String,Map<String,String>> mp:store.entrySet()) {
			System.out.println(mp.getKey());
			deepCopy.put(mp.getKey(),new HashMap<>());
			Map<String,String> cp=deepCopy.getOrDefault(mp.getKey(),new HashMap<>());
			for (Map.Entry<String, String> m : mp.getValue().entrySet()){
				cp.put(m.getKey(),m.getValue());
			}
		}

		tnxStack.push(deepCopy);
	}
	public void commit(){
		if (!tnxStack.isEmpty()){
			tnxStack.pop();
		}
	}
	public void rollback(){
		if(!tnxStack.isEmpty()){
			store=tnxStack.pop();
		}
	}

	public static void main(String[] args) {
		Solution solution=new Solution();
		solution.updateCol("customer","age","21");
		solution.updateCol("customer","name ","alice");
		solution.updateCol("user","name ","alice");
		solution.display();

		solution.begin();
		solution.delete("customer");
		solution.rollback();
		solution.display();

		solution.commit();

	}
}
