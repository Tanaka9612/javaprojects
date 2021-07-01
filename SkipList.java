// Name:Tanaka Humbani
// Student number:15057420

import java.util.Random;

@SuppressWarnings("unchecked")
public class SkipList<T extends Comparable<? super T>>
{

	public int maxLevel;
	public SkipListNode<T>[] root;
	private int[] powers;
	private Random rd = new Random();

	SkipList(int i)
	{
		maxLevel = i;
		root = new SkipListNode[maxLevel];
		powers = new int[maxLevel];
		for (int j = 0; j < i; j++)
			root[j] = null;
		choosePowers();
		rd.setSeed(1230456789);
	}

	SkipList()
	{
		this(4);
	}

	public void choosePowers()
	{
		powers[maxLevel-1] = (2 << (maxLevel-1)) - 1;
		for (int i = maxLevel - 2, j = 0; i >= 0; i--, j++)
			powers[i] = powers[i+1] - (2 << j);
	}

	public int chooseLevel()
	{
		int i, r = Math.abs(rd.nextInt()) % powers[maxLevel-1] + 1;
		for (i = 1; i < maxLevel; i++)
		{
			if(r < powers[i])
				return i-1;
		}
		return i-1;
	}

	////// You may not change any code above this line //////

	////// Implement the functions below this line //////

	public boolean isEmpty()
	{
		//Your code goes here
		return root[0] == null;
	}

	public void insert(T key)
	{
		//Your code goes here
		
		SkipListNode<T> [] current = new SkipListNode[maxLevel];
		SkipListNode<T> [] previous = new SkipListNode[maxLevel];
		SkipListNode<T> newnode;
		int level, j;
		current[maxLevel-1] = root[maxLevel-1];
		previous[maxLevel-1] = null;
		for(level = maxLevel-1 ; level >= 0 ; level--) {
			while(current[level] != null && current[level].key.compareTo(key)< 0 ) {
				previous[level] = current[level];
				current[level] = current[level].next[level];
			}
			if(current[level] != null && current[level].key.equals(key))
				return;
			if(level > 0)
				if(previous[level] == null) {
					current[level-1] = root[level-1];
					previous[level-1] = null;
				}
				else {
					current[level-1] = previous[level].next[level-1];
					previous[level-1] = previous[level];
				}
		}
		level = chooseLevel();
		newnode = new SkipListNode<T>(key, level+1);
		for(j = 0 ; j <= level ; j++) {
			newnode.next[j] = current[j];
			if(previous[j] == null)
				root[j] = newnode;
			else
				previous[j].next[j] = newnode;
		}
	}

	public boolean delete(T key)
	{
		SkipListNode<T> node = null,ptr = null;
		int lvl;
		if(isEmpty())
			return false;
		else if(!isEmpty() && search(key) == key)
		{
			node = key;
			SkipListNode<T>[] prev = new SkipListNode[maxLevel -1];
			ptr = root[maxLevel-1];
			
			for(int i = maxLevel -1; i >= 0 ; i--)
			{
				while(ptr.next[i] != node)
					ptr = ptr.next[i];
				
				prev[i] = ptr;
			}
			for(int j = 0 ; j < maxLevel-1 ; j++)
				prev[j].next[j] = node.next[j]; 
			
			return true;	
		}
		else
		return false;
	}
		

	public T first()
	{
		//Your code goes here
		if(root[0] != null)
			return (T)root[0].key;
		else
			return null;
	}

	public T last()
	{
		SkipListNode<T> current = root[0];
		while(current.next[0] != null) {
			current = current.next[0];
		}
		return current.key;
	}
	

	public T search(T key)
	{
		//Your code goes here
		int level;
		SkipListNode<T> previous, current;
		for(level = maxLevel-1 ; level >= 0 && root[level]== null ; level--);
		previous = current = root[level];
		while(true) {
			if(key.equals(current.key))
				return current.key;
			else if(key.compareTo(current.key)<0) {
				if(level == 0)
					return null;
				else if(current == root[level])
					current = root[--level];
				else
					current = previous.next[--level];
			}
			else {
				previous = current;
				if(current.next[level] != null)
					current = current.next[level];
				else {
					for(level-- ; level >= 0 && current.next[level] == null ; level--);
					if(level >= 0 )
						current = current.next[level];
					else 
						return null;
					
				}
			}
		}
	}
}