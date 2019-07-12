public class NumberRangeSummarizer_init implements NumberRangeSummarizer<Integer, List<String>, List<String>>{

    private int last = 0;
    private LinkedList<Integer> intermediate = new LinkedList<>();

    @Override
    public Supplier<List<String>> supplier(){
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<String>, Integer> accumulator(){
        return ( finalList, current ) -> {
            if( current - last == 1 ){ // check if adjacent to last value
                intermediate.add(current);
            } else{
                if( intermediate.size() > 2 ){
                    finalList.add(intermediate.getFirst() + "-" + intermediate.getLast()); // add new range
                } else{
                    addLeftOverValues(finalList);
                }
                intermediate.clear();
                intermediate.add(current);
            }
            last = current;
        };
    }

    @Override
    public BinaryOperator<List<String>> combiner(){
        return (list, list2) -> {
            list.addAll(list2);
            return list;
        };
    }

    @Override
    public Function<List<String>, List<String>> finisher(){
        return ( finalList ) -> {
            if( !intermediate.isEmpty() ){
                addLeftOverValues(finalList);
            }
            return finalList;
        };
    }

    @Override
    public Set<Characteristics> characteristics(){
        return EnumSet.noneOf(Characteristics.class);
    }

    private void addLeftOverValues( List<String> list ){
        list.addAll(
            intermediate.stream()
                .map(String::valueOf)
                .collect(NumberRangeSummarizer.toList())
       );
    }
}


ist<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 12, 13, 14, 19);
System.out.println(list.stream().collect(new RangeCollector()));
