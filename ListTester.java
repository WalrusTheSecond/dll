
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * A unit test class for lists that implement IndexedUnsortedList.
 * This is a set of black box tests that should work for any implementation
 * of this interface.
 * 
 * NOTE: One example test is given for each interface method using a new list to
 * get you started.
 * 
 * @author mvail, mhthomas, awinters
 */
@SuppressWarnings("deprecated")
public class ListTester {
	// possible lists that could be tested
	private static enum ListToUse {
		goodList, badList, arrayList, singleLinkedList, doubleLinkedList,
	};

	// TODO: THIS IS WHERE YOU CHOOSE WHICH LIST TO TEST
	private final static ListToUse LIST_TO_USE = ListToUse.doubleLinkedList;

	// possible results expected in tests
	private enum Result {
		IndexOutOfBounds, IllegalState, NoSuchElement,
		ConcurrentModification, UnsupportedOperation,
		NoException, UnexpectedException,
		True, False, Pass, Fail,
		MatchingValue,
		ValidString
	};

	// named elements for use in tests
	private static final Integer ELEMENT_A = 1;
	private static final Integer ELEMENT_B = 2;
	private static final Integer ELEMENT_C = 3;
	private static final Integer ELEMENT_D = 4;
	private static final Integer ELEMENT_X = -1;// element that should appear in no lists
	private static final Integer ELEMENT_Z = -2;// element that should appear in no lists

	// determine whether to include ListIterator functionality tests
	private final boolean SUPPORTS_LIST_ITERATOR; // initialized in constructor

	// tracking number of tests and test results
	private int passes = 0;
	private int failures = 0;
	private int totalRun = 0;

	private int secTotal = 0;
	private int secPasses = 0;
	private int secFails = 0;

	// control output - modified by command-line args
	private boolean printFailuresOnly = true;
	private boolean showToString = true;
	private boolean printSectionSummaries = true;

	/**
	 * Valid command line args include:
	 * -a : print results from all tests (default is to print failed tests, only)
	 * -s : hide Strings from toString() tests
	 * -m : hide section summaries in output
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		// to avoid every method being static
		ListTester tester = new ListTester(args);
		tester.runTests();

	}

	/**
	 * tester constructor
	 * 
	 * @param args command line args
	 */
	public ListTester(String[] args) {
		for (String arg : args) {
			if (arg.equalsIgnoreCase("-a"))
				printFailuresOnly = false;
			if (arg.equalsIgnoreCase("-s"))
				showToString = false;
			if (arg.equalsIgnoreCase("-m"))
				printSectionSummaries = false;
		}
		switch (LIST_TO_USE) {
			case doubleLinkedList:
				SUPPORTS_LIST_ITERATOR = true;
				break;
			default:
				SUPPORTS_LIST_ITERATOR = false;
				break;
		}
	}

	/**
	 * Print test results in a consistent format
	 * 
	 * @param testDesc description of the test
	 * @param result   indicates if the test passed or failed
	 */
	private void printTest(String testDesc, boolean result) {
		totalRun++;
		if (result) {
			passes++;
		} else {
			failures++;
		}
		if (!result || !printFailuresOnly) {
			System.out.printf("%-46s\t%s\n", testDesc, (result ? "   PASS" : "***FAIL***"));
		}
	}

	/** Print a final summary */
	private void printFinalSummary() {
		String verdict = String.format("\nTotal Tests Run: %d,  Passed: %d (%.1f%%),  Failed: %d\n",
				totalRun, passes, passes * 100.0 / totalRun, failures);
		String line = "";
		for (int i = 0; i < verdict.length(); i++) {
			line += "-";
		}
		System.out.println(line);
		System.out.println(verdict);
	}

	/** Print a section summary */
	private void printSectionSummary() {
		secTotal = totalRun - secTotal;
		secPasses = passes - secPasses;
		secFails = failures - secFails;
		System.out.printf("\nSection Tests: %d,  Passed: %d,  Failed: %d\n", secTotal, secPasses, secFails);
		secTotal = totalRun; // reset for next section
		secPasses = passes;
		secFails = failures;
		System.out.printf("Tests Run So Far: %d,  Passed: %d (%.1f%%),  Failed: %d\n",
				totalRun, passes, passes * 100.0 / totalRun, failures);
	}

	/////////////////////
	// XXX runTests()
	/////////////////////

	/**
	 * Run tests to confirm required functionality from list constructors and
	 * methods
	 */
	private void runTests() {
		// Possible list contents after a scenario has been set up
		Integer[] LIST_A = { ELEMENT_A };
		String STRING_A = "A";
		Integer[] LIST_AB = { ELEMENT_A, ELEMENT_B };
		String STRING_AB = "AB";
		Integer[] LIST_BA = { ELEMENT_B, ELEMENT_A };
		String STRING_BA = "BA";
		Integer[] LIST_B = { ELEMENT_B };
		String STRING_B = "B";
		Integer[] LIST_BC = { ELEMENT_B, ELEMENT_C };
		String STRING_BC = "BC";
		Integer[] LIST_AC = { ELEMENT_A, ELEMENT_C };
		String STRING_AC = "AC";
		Integer[] LIST_ABC = { ELEMENT_A, ELEMENT_B, ELEMENT_C };
		String STRING_ABC = "ABC";
		Integer[] LIST_ABD = { ELEMENT_A, ELEMENT_B, ELEMENT_D };
		String STRING_ABD = "ABD";
		Integer[] LIST_CAB = { ELEMENT_C, ELEMENT_A, ELEMENT_B };
		String STRING_CAB = "CAB";
		Integer[] LIST_DBC = { ELEMENT_D, ELEMENT_B, ELEMENT_C };
		String STRING_DBC = "DBC";

		// newly constructed empty list
		testEmptyList(newList, "newList");
		// empty to 1-element list
		testSingleElementList(emptyList_addToFrontA_A, "emptyList_addToFrontA_A", LIST_A, STRING_A);
		testSingleElementList(emptyList_addToReartA_A, "emptyList_addToRearA_A", LIST_A, STRING_A);
		// 1-element to empty list
		testEmptyList(A_removeA, "A_removeA");
		testEmptyList(A_removeLast, "A_removeLast");
		// 1-element to 2-element
		testTwoElementList(A_addToFrontB_BA, "A_addToFrontB_BA", LIST_BA, STRING_BA);
		testTwoElementList(A_addToRearB_AB, "A_addToRearB_AB", LIST_AB, STRING_AB);
		// 1-element to changed 1-element via set()
		testSingleElementList(A_setB_B, "A_setB", LIST_B, STRING_B);
		// 2-element to 1-element
		testSingleElementList(BA_removeFirst_A, "BA_removeFirst_A", LIST_A, STRING_A);
		testSingleElementList(BA_removeLast_B, "BA_removeLast_B", LIST_B, STRING_B);
		// 2-element to 3-element
		testThreeElementList(AB_addFrontC_CAB, "AB_addFrontC_CAB", LIST_CAB, STRING_CAB);
		testThreeElementList(AB_addToRearC_ABC, "AB_addToRearC_ABC", LIST_ABC, STRING_ABC);
		testThreeElementList(AB_addCIndex0_CAB, "AB_addCIndex0_CAB", LIST_CAB, STRING_CAB);
		testThreeElementList(AB_addToFrontC_CAB, "AB_addToFrontC_CAB", LIST_CAB, STRING_CAB);
		// 2-element to changed 2-element via set()
		testTwoElementList(BA_setAtoC_BC, "BA_setAtoC_BC", LIST_BC, STRING_BC);
		// 3-element to 2-element
		testTwoElementList(ABC_removeFirst_BC, "ABC_removeFirst_BC", LIST_BC, STRING_BC);
		testTwoElementList(ABC_removeC_AB, "ABC_removeC_AB", LIST_AB, STRING_AB);
		// 3-element to changed 3-element via set()
		testThreeElementList(ABC_setCtoD_ABD, "ABC_setCtoD_ABD", LIST_ABD, STRING_ABD);

		// Iterator Change Scenarios
		testEmptyList(A_IterRemoveAfterNext_, "A_IterRemoveAfterNext_");
		testEmptyList(A_IterRemoveAfterPrev_, "A_IterRemoveAfterPrev_");
		testSingleElementList(AB_iterRemoveAfterNext_B, "AB_iterRemoveAfterNext_B", LIST_B, STRING_B);
		testSingleElementList(AB_iter1RemoveAfterNext_A, "AB_iter1RemoveAfterNext_A", LIST_A, STRING_A);
		testSingleElementList(BA_Iter2RemoveAfterPrev_B, "BA_Iter2RemoveAfterPrev_B", LIST_B, STRING_B);
		testTwoElementList(ABC_IterRemoveAfterPrev_BC, "ABC_IterRemoveAfterPrev_BC", LIST_BC, STRING_BC);
		testTwoElementList(ABC_IterRemoveAfterPrev_AC, "ABC_IterRemoveAfterPrev_AC", LIST_AC, STRING_AC);
		testSingleElementList(_IterAddA_A, "_IterAddA_A", LIST_A, STRING_A);
		testThreeElementList(ABC_IterSetDAfterPrevious_DBC, "ABC_IterSetDAfterPrevious_DBC", LIST_DBC, STRING_DBC);
		testThreeElementList(ABC_IterSetDAfterNext_DBC, "ABC_IterSetDAfterNext_DBC", LIST_DBC, STRING_DBC);
		testThreeElementList(AB_IterAddC_CAB, "AB_IterAddC_CAB", LIST_CAB, STRING_CAB);

		// Iterator concurrency tests
		test_IterConcurrency();
		if (SUPPORTS_LIST_ITERATOR) {
			test_ListIterConcurrency();
		}

		// report final verdict
		printFinalSummary();
	}

	//////////////////////////////////////
	// XXX SCENARIO BUILDERS
	//////////////////////////////////////

	/**
	 * Returns a IndexedUnsortedList for the "new empty list" scenario.
	 * Scenario: no list -> constructor -> [ ]
	 * 
	 * NOTE: Comment out cases for any implementations not currently available
	 *
	 * @return a new, empty IndexedUnsortedList
	 */
	private IndexedUnsortedList<Integer> newList() {
		IndexedUnsortedList<Integer> listToUse;
		switch (LIST_TO_USE) {
			case goodList:
				listToUse = new GoodList<Integer>();
				break;
			case badList:
				listToUse = new BadList<Integer>();
				break;
			case singleLinkedList:
				listToUse = new IUSingleLinkedList<Integer>();
				break;
			case doubleLinkedList:
				listToUse = new IUDoubleLinkedList<Integer>();
				break;
			default:
				listToUse = null;
		}
		return listToUse;
	}

	// The following creates a "lambda" reference that allows us to pass a scenario
	// builder method as an argument. You don't need to worry about how it works -
	// just make sure each scenario building method has a corresponding Scenario
	// assignment statement as in these examples.
	private Scenario<Integer> newList = () -> newList();

	/**
	 * Scenario: [A] -> remove(A) -> []
	 * 
	 * @return [] after remove(A)
	 */
	private IndexedUnsortedList<Integer> A_removeA() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.remove(ELEMENT_A);
		return list;
	}

	private Scenario<Integer> A_removeA = () -> A_removeA();

	/**
	 * Scenario: [A] -> removeLast() -> []
	 * 
	 * @return [] after removeLast()
	 */
	private IndexedUnsortedList<Integer> A_removeLast() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.removeLast();
		return list;
	}

	private Scenario<Integer> A_removeLast = () -> A_removeLast();

	/**
	 * Scenario: empty list -> addToFront(A) -> [A]
	 * 
	 * @return [A] after addToFront(A)
	 */
	private IndexedUnsortedList<Integer> emptyList_addToFrontA_A() {
		IndexedUnsortedList<Integer> list = newList();
		list.addToFront(ELEMENT_A);
		return list;
	}

	private Scenario<Integer> emptyList_addToFrontA_A = () -> emptyList_addToFrontA_A();

	/**
	 * Scenario: empty list -> addToRear(A) -> [A]
	 * 
	 * @return [A] after addToRear(A)
	 */
	private IndexedUnsortedList<Integer> emptyList_addToReartA_A() {
		IndexedUnsortedList<Integer> list = newList();
		list.addToRear(ELEMENT_A);
		return list;
	}

	private Scenario<Integer> emptyList_addToReartA_A = () -> emptyList_addToReartA_A();

	/**
	 * Scenario: [A] -> addToFront(B) -> [B,A]
	 * 
	 * @return [B,A] after addToFront(B)
	 */
	private IndexedUnsortedList<Integer> A_addToFrontB_BA() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.addToFront(ELEMENT_B);
		return list;
	}

	private Scenario<Integer> A_addToFrontB_BA = () -> A_addToFrontB_BA();

	/**
	 * Scenario: [A] -> addToRear(B) -> [A,B]
	 * 
	 * @return [A,B] after addToRear(B)
	 */
	private IndexedUnsortedList<Integer> A_addToRearB_AB() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.addToRear(ELEMENT_B);
		return list;
	}

	private Scenario<Integer> A_addToRearB_AB = () -> A_addToRearB_AB();

	/**
	 * Scenario: [A,B] -> add(0,C) -> [C,A,B]
	 * 
	 * @return [C,A,B] after add(0,C)
	 */
	private IndexedUnsortedList<Integer> AB_addCIndex0_CAB() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.add(0, ELEMENT_C);
		return list;
	}

	private Scenario<Integer> AB_addCIndex0_CAB = () -> AB_addCIndex0_CAB();

	/**
	 * Scenario: [A,B] -> addToFront(C) -> [C,A,B]
	 * 
	 * @return [C,A,B] after addToFront (C)
	 */
	private IndexedUnsortedList<Integer> AB_addFrontC_CAB() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.addToFront(ELEMENT_C);
		return list;
	}

	private Scenario<Integer> AB_addFrontC_CAB = () -> AB_addFrontC_CAB();

	/**
	 * Scenario: [A,B] -> addToFront(C) -> [C,A,B]
	 * 
	 * @return [C,A,B] after addToFront(C)
	 */
	private IndexedUnsortedList<Integer> AB_addToFrontC_CAB() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.addToFront(ELEMENT_C);
		return list;
	}

	private Scenario<Integer> AB_addToFrontC_CAB = () -> AB_addToFrontC_CAB();

	/**
	 * Scenario: [A,B] -> addToRear(C) -> [A,B,C]
	 * 
	 * @return [A,B,C] after addToRear(C)
	 */
	private IndexedUnsortedList<Integer> AB_addToRearC_ABC() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.addToRear(ELEMENT_C);
		return list;
	}

	private Scenario<Integer> AB_addToRearC_ABC = () -> AB_addToRearC_ABC();

	/**
	 * Scenario: [A,B,C] -> remove(C) -> [A,B]
	 * 
	 * @return [A,B] after remove(C)
	 */
	private IndexedUnsortedList<Integer> ABC_removeC_AB() {
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		list.remove(ELEMENT_C);
		return list;
	}

	private Scenario<Integer> ABC_removeC_AB = () -> ABC_removeC_AB();

	/**
	 * Scenario: [A,B,C] -> removeFirst() -> [B,C]
	 * 
	 * @return [B,C] after removeFirst()
	 */
	private IndexedUnsortedList<Integer> ABC_removeFirst_BC() {
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		list.removeFirst();
		return list;
	}

	private Scenario<Integer> ABC_removeFirst_BC = () -> ABC_removeFirst_BC();

	/**
	 * Scenario: [A,B,C] -> set(2,D) -> [A,B,D]
	 * 
	 * @return [A,B,D] after set(2,D)
	 */
	private IndexedUnsortedList<Integer> ABC_setCtoD_ABD() {
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		list.set(2, ELEMENT_D);
		return list;
	}

	private Scenario<Integer> ABC_setCtoD_ABD = () -> ABC_setCtoD_ABD();

	/**
	 * Scenario: [A] -> set(B) -> [B]
	 * 
	 * @return [B] after set(B)
	 */
	private IndexedUnsortedList<Integer> A_setB_B() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.set(0, ELEMENT_B);
		return list;
	}

	private Scenario<Integer> A_setB_B = () -> A_setB_B();

	/**
	 * Scenario: [B,A] -> removeFirst() -> [A]
	 * 
	 * @return [A] after removeFirst()
	 */
	private IndexedUnsortedList<Integer> BA_removeFirst_A() {
		IndexedUnsortedList<Integer> list = A_addToFrontB_BA();
		list.removeFirst();
		return list;
	}

	private Scenario<Integer> BA_removeFirst_A = () -> BA_removeFirst_A();

	/**
	 * Scenario: [B,A] -> removeLast() -> [B]
	 * 
	 * @return [B] after removeLast()
	 */
	private IndexedUnsortedList<Integer> BA_removeLast_B() {
		IndexedUnsortedList<Integer> list = A_addToFrontB_BA();
		list.removeLast();
		return list;
	}

	private Scenario<Integer> BA_removeLast_B = () -> BA_removeLast_B();

	/**
	 * Scenario: [B,A] -> set(A,C) -> [B,C]
	 * 
	 * @return [B,C] after set(A,C)
	 */
	private IndexedUnsortedList<Integer> BA_setAtoC_BC() {
		IndexedUnsortedList<Integer> list = A_addToFrontB_BA();
		list.set(ELEMENT_A, ELEMENT_C);
		return list;
	}

	private Scenario<Integer> BA_setAtoC_BC = () -> BA_setAtoC_BC();

	// ITERATOR SCENARIO TESTS

	/**
	 * Scenario: [A] -> iterator remove() after next() returns A -> []
	 * 
	 * @return [] after iterator remove() after next()
	 */
	private IndexedUnsortedList<Integer> A_IterRemoveAfterNext_() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		Iterator<Integer> iter = list.iterator();
		iter.next();
		iter.remove();
		return list;
	}

	private Scenario<Integer> A_IterRemoveAfterNext_ = () -> A_IterRemoveAfterNext_();

	/**
	 * Scenario: [A,B] -> iterator remove() after next() returns -> [B]
	 * 
	 * @return [B] after iterator remove() after next()
	 */
	private IndexedUnsortedList<Integer> AB_iterRemoveAfterNext_B() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		Iterator<Integer> iter = list.iterator();
		iter.next();
		iter.remove();
		return list;
	}

	private Scenario<Integer> AB_iterRemoveAfterNext_B = () -> AB_iterRemoveAfterNext_B();

	/**
	 * Scenario: [A,B] -> iterator remove() after next() returns B -> [A]
	 * 
	 * @return [A] after iterator remove() after next()
	 */
	private IndexedUnsortedList<Integer> AB_iter1RemoveAfterNext_A() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		ListIterator<Integer> iter = list.listIterator(1);
		iter.next();
		iter.remove();
		return list;
	}

	private Scenario<Integer> AB_iter1RemoveAfterNext_A = () -> AB_iter1RemoveAfterNext_A();

	/**
	 * Scenario: [A] -> iterator remove() after previous() returns A -> []
	 * 
	 * @return [] after iterator remove() after previous()
	 */
	private IndexedUnsortedList<Integer> A_IterRemoveAfterPrev_() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		ListIterator<Integer> iter = list.listIterator(1);
		iter.previous();
		iter.remove();
		return list;
	}

	private Scenario<Integer> A_IterRemoveAfterPrev_ = () -> A_IterRemoveAfterPrev_();

	/**
	 * Scenario: [B,A] -> iterator remove() after previous() returns A -> [B]
	 * 
	 * @return [B] after iterator remove() after previous()
	 */
	private IndexedUnsortedList<Integer> BA_Iter2RemoveAfterPrev_B() {
		IndexedUnsortedList<Integer> list = A_addToFrontB_BA();
		ListIterator<Integer> iter = list.listIterator(2);
		iter.previous();
		iter.remove();
		return list;
	}

	private Scenario<Integer> BA_Iter2RemoveAfterPrev_B = () -> BA_Iter2RemoveAfterPrev_B();

	/**
	 * Scenario: [A,B,C] -> iterator remove() after previous() returns A -> [B,C]
	 * 
	 * @return [B,C] after iterator remove() after previous()
	 */
	private IndexedUnsortedList<Integer> ABC_IterRemoveAfterPrev_BC() {
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		ListIterator<Integer> iter = list.listIterator(1);
		iter.previous();
		iter.remove();
		return list;
	}

	private Scenario<Integer> ABC_IterRemoveAfterPrev_BC = () -> ABC_IterRemoveAfterPrev_BC();

	/**
	 * Scenario: [A,B,C] -> iterator remove() after previous() returns B -> [A,C]
	 * 
	 * @return [A,C] after iterator remove() after previous()
	 */
	private IndexedUnsortedList<Integer> ABC_IterRemoveAfterPrev_AC() {
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		ListIterator<Integer> iter = list.listIterator(2);
		iter.previous();
		iter.remove();
		return list;
	}

	private Scenario<Integer> ABC_IterRemoveAfterPrev_AC = () -> ABC_IterRemoveAfterPrev_AC();

	/**
	 * Scenario: [] -> iterator add(A) -> [A]
	 * 
	 * @return [A] after iterator add(A)
	 */
	private IndexedUnsortedList<Integer> _IterAddA_A() {
		IndexedUnsortedList<Integer> list = newList();
		ListIterator<Integer> iter = list.listIterator();
		iter.add(ELEMENT_A);
		return list;
	}

	private Scenario<Integer> _IterAddA_A = () -> _IterAddA_A();

	/**
	 * Scenario: [A,B,C] -> iterator set(D) after previous -> [D,B,C]
	 * 
	 * @return [D,B,C] after iterator set(D)
	 */
	private IndexedUnsortedList<Integer> ABC_IterSetDAfterPrevious_DBC() {
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		ListIterator<Integer> iter = list.listIterator(1);
		iter.previous();
		iter.set(ELEMENT_D);
		return list;
	}

	private Scenario<Integer> ABC_IterSetDAfterPrevious_DBC = () -> ABC_IterSetDAfterPrevious_DBC();

	/**
	 * Scenario: [A,B,C] -> iterator set(D) after next -> [D,B,C]
	 * 
	 * @return [D,B,C] after iterator set(D) after next
	 */
	private IndexedUnsortedList<Integer> ABC_IterSetDAfterNext_DBC() {
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		ListIterator<Integer> iter = list.listIterator();
		iter.next();
		iter.set(ELEMENT_D);
		return list;
	}

	private Scenario<Integer> ABC_IterSetDAfterNext_DBC = () -> ABC_IterSetDAfterNext_DBC();

	/**
	 * Scenario: [A,B] -> iterator add(C) new Iterator -> [A,B,C]
	 * 
	 * @return [A,B,C] after iterator add(C) new Iterator
	 */
	private IndexedUnsortedList<Integer> AB_IterAddC_CAB() {
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		ListIterator<Integer> iter = list.listIterator();

		iter.add(ELEMENT_C);
		return list;
	}

	private Scenario<Integer> AB_IterAddC_CAB = () -> AB_IterAddC_CAB();

	/////////////////////////////////
	// XXX Tests for 0-element list
	/////////////////////////////////

	/**
	 * Run all tests on scenarios resulting in an empty list
	 * 
	 * @param scenario     lambda reference to scenario builder method
	 * @param scenarioName name of the scenario being tested
	 */
	private void testEmptyList(Scenario<Integer> scenario, String scenarioName) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			// IndexedUnsortedList
			printTest(scenarioName + "_testRemoveFirst", testRemoveFirst(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testRemoveLast", testRemoveLast(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testRemoveX", testRemoveElement(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.True));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 0));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			printTest(scenarioName + "_testAddToFront",
					testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX",
					testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAddAtIndexNeg1",
					testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex0",
					testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1",
					testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddX", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testRemoveNeg1",
					testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemove0",
					testRemoveIndex(scenario.build(), 0, null, Result.IndexOutOfBounds));
			// Iterator
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.False));
			printTest(scenarioName + "_testIterNext",
					testIterNext(scenario.build().iterator(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testIterRemove",
					testIterRemove(scenario.build().iterator(), Result.IllegalState));
			// ListIterator
			if (SUPPORTS_LIST_ITERATOR) {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
				printTest(scenarioName + "_testListIterNeg1",
						testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
				printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIterHasNext",
						testIterHasNext(scenario.build().listIterator(), Result.False));
				printTest(scenarioName + "_testListIterNext",
						testIterNext(scenario.build().listIterator(), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIterRemove",
						testIterRemove(scenario.build().listIterator(), Result.IllegalState));
				printTest(scenarioName + "_testListIterHasPrevious",
						testListIterHasPrevious(scenario.build().listIterator(), Result.False));
				printTest(scenarioName + "_testListIterPrevious",
						testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIterAdd",
						testListIterAdd(scenario.build().listIterator(), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterSet",
						testListIterSet(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIterNextIndex",
						testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0NextIndex",
						testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIterPreviousIndex",
						testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0PreviousIndex",
						testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
			} else {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.UnsupportedOperation));
				printTest(scenarioName + "_testListIter0",
						testListIter(scenario.build(), 0, Result.UnsupportedOperation));
			}
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	//////////////////////////////////
	// XXX Tests for 1-element list
	//////////////////////////////////

	/**
	 * Run all tests on scenarios resulting in a single element list
	 * 
	 * @param scenario       lambda reference to scenario builder method
	 * @param scenarioName   name of the scenario being tested
	 * @param contents       elements expected in the list after scenario has been
	 *                       set up
	 * @param contentsString contains character labels corresponding to values in
	 *                       contents
	 */
	private void testSingleElementList(Scenario<Integer> scenario, String scenarioName, Integer[] contents,
			String contentsString) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			// IndexedUnsortedList
			printTest(scenarioName + "_testRemoveFirst",
					testRemoveFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveLast",
					testRemoveLast(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(0),
					testRemoveElement(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveX",
					testRemoveElement(scenario.build(), ELEMENT_X, Result.NoSuchElement));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testContains" + contentsString.charAt(0),
					testContains(scenario.build(), contents[0], Result.True));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.False));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 1));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			printTest(scenarioName + "_testAddToFront",
					testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(0),
					testAddAfter(scenario.build(), contents[0], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX",
					testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAddAtIndexNeg1",
					testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex0",
					testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1",
					testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex2",
					testAddAtIndex(scenario.build(), 2, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet1", testSet(scenario.build(), 1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAdd", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testGet1", testGet(scenario.build(), 1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(0),
					testIndexOf(scenario.build(), contents[0], 0));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testRemoveNeg1",
					testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemove0",
					testRemoveIndex(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove1",
					testRemoveIndex(scenario.build(), 1, null, Result.IndexOutOfBounds));
			// Iterator
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.True));
			printTest(scenarioName + "_testIterNext",
					testIterNext(scenario.build().iterator(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testIterRemove",
					testIterRemove(scenario.build().iterator(), Result.IllegalState));
			printTest(scenarioName + "_iterNext_testIterHasNext",
					testIterHasNext(iterAfterNext(scenario.build(), 1), Result.False));
			printTest(scenarioName + "_iterNext_testIterNext",
					testIterNext(iterAfterNext(scenario.build(), 1), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNext_testIterRemove",
					testIterRemove(iterAfterNext(scenario.build(), 1), Result.NoException));
			printTest(scenarioName + "_iterNextRemove_testIterHasNext",
					testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.False));
			printTest(scenarioName + "_iterNextRemove_testIterNext",
					testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextRemove_testIterRemove",
					testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.IllegalState));
			// ListIterator
			if (SUPPORTS_LIST_ITERATOR) {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
				printTest(scenarioName + "_testListIterNeg1",
						testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
				printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.NoException));
				printTest(scenarioName + "_testListIter2", testListIter(scenario.build(), 2, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIterHasNext",
						testIterHasNext(scenario.build().listIterator(), Result.True));
				printTest(scenarioName + "_testListIterNext",
						testIterNext(scenario.build().listIterator(), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIterNextIndex",
						testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIterHasPrevious",
						testListIterHasPrevious(scenario.build().listIterator(), Result.False));
				printTest(scenarioName + "_testListIterPrevious",
						testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIterPreviousIndex",
						testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
				printTest(scenarioName + "_testListIterRemove",
						testIterRemove(scenario.build().listIterator(), Result.IllegalState));
				printTest(scenarioName + "_testListIterAdd",
						testListIterAdd(scenario.build().listIterator(), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterSet",
						testListIterSet(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIterNextRemove",
						testIterRemove(listIterAfterNext(scenario.build().listIterator(), 1), Result.NoException));
				printTest(scenarioName + "_testListIterNextAdd", testListIterAdd(
						listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNextSet", testListIterSet(
						listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNextRemoveRemove",
						testIterRemove(listIterAfterRemove(listIterAfterNext(scenario.build().listIterator(), 1)),
								Result.IllegalState));
				printTest(scenarioName + "_testListIterNextPreviousRemove",
						testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1),
								Result.NoException));
				printTest(scenarioName + "_testListIterNextPreviousRemoveRemove", testIterRemove(
						listIterAfterRemove(
								listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1)),
						Result.IllegalState));
				printTest(scenarioName + "_testListIterNextPreviousAdd",
						testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNextPreviousSet",
						testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0HasNext",
						testIterHasNext(scenario.build().listIterator(0), Result.True));
				printTest(scenarioName + "_testListIter0Next",
						testIterNext(scenario.build().listIterator(0), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIter0NextIndex",
						testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0HasPrevious",
						testListIterHasPrevious(scenario.build().listIterator(0), Result.False));
				printTest(scenarioName + "_testListIter0Previous",
						testListIterPrevious(scenario.build().listIterator(0), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIter0PreviousIndex",
						testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0Remove",
						testIterRemove(scenario.build().listIterator(0), Result.IllegalState));
				printTest(scenarioName + "_testListIter0Add",
						testListIterAdd(scenario.build().listIterator(0), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0Set",
						testListIterSet(scenario.build().listIterator(0), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIter0NextRemove",
						testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 1), Result.NoException));
				printTest(scenarioName + "_testListIter0NextAdd", testListIterAdd(
						listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0NextSet", testListIterSet(
						listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0NextPreviousRemove",
						testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1),
								Result.NoException));
				printTest(scenarioName + "_testListIter0NextPreviousAdd",
						testListIterAdd(
								listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0NextPreviousSet",
						testListIterSet(
								listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1HasNext",
						testIterHasNext(scenario.build().listIterator(1), Result.False));
				printTest(scenarioName + "_testListIter1Next",
						testIterNext(scenario.build().listIterator(1), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIter1NextIndex",
						testListIterNextIndex(scenario.build().listIterator(1), 1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter1HasPrevious",
						testListIterHasPrevious(scenario.build().listIterator(1), Result.True));
				printTest(scenarioName + "_testListIter1Previous",
						testListIterPrevious(scenario.build().listIterator(1), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIter1PreviousIndex",
						testListIterPreviousIndex(scenario.build().listIterator(1), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIter1Remove",
						testIterRemove(scenario.build().listIterator(1), Result.IllegalState));
				printTest(scenarioName + "_testListIter1Add",
						testListIterAdd(scenario.build().listIterator(1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1Set",
						testListIterSet(scenario.build().listIterator(1), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIter1PreviousRemove",
						testIterRemove(listIterAfterPrevious(scenario.build().listIterator(1), 1), Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousAdd", testListIterAdd(
						listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousSet", testListIterSet(
						listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousNextRemove",
						testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1),
								Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousNextAdd",
						testListIterAdd(
								listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousNextSet",
						testListIterSet(
								listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1),
								ELEMENT_X, Result.NoException));
			} else {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.UnsupportedOperation));
				printTest(scenarioName + "_testListIter0",
						testListIter(scenario.build(), 0, Result.UnsupportedOperation));
			}

		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	/////////////////////////////////
	// XXX Tests for 2-element list
	/////////////////////////////////

	/**
	 * Run all tests on scenarios resulting in a two-element list
	 * 
	 * @param scenario       lambda reference to scenario builder method
	 * @param scenarioName   name of the scenario being tested
	 * @param contents       elements expected in the list after scenario has been
	 *                       set up
	 * @param contentsString contains character labels corresponding to values in
	 *                       contents
	 */
	private void testTwoElementList(Scenario<Integer> scenario, String scenarioName, Integer[] contents,
			String contentsString) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			// TODO: tests for scenarios ending in a 2-element list
			printTest(scenarioName + "_testAddToFront",
					testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(0),
					testAddAfter(scenario.build(), contents[0], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(1),
					testAddAfter(scenario.build(), contents[1], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX",
					testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAdd", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex0",
					testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1",
					testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex2",
					testAddAtIndex(scenario.build(), 2, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex3",
					testAddAtIndex(scenario.build(), 3, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemoveFirst",
					testRemoveFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveLast",
					testRemoveLast(scenario.build(), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveAt" + contentsString.charAt(0),
					testRemoveElement(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveAt" + contentsString.charAt(1),
					testRemoveElement(scenario.build(), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveX",
					testRemoveElement(scenario.build(), ELEMENT_X, Result.NoSuchElement));
			printTest(scenarioName + "_testSetNeg", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet1", testSet(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSetX", testSet(scenario.build(), 2, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testGet1", testGet(scenario.build(), 1, contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testGet2", testGet(scenario.build(), 2, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOf0" + contentsString.charAt(0),
					testIndexOf(scenario.build(), contents[0], 0));
			printTest(scenarioName + "_testIndexOf1" + contentsString.charAt(1),
					testIndexOf(scenario.build(), contents[1], 1));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testContains" + contentsString.charAt(0),
					testContains(scenario.build(), contents[0], Result.True));
			printTest(scenarioName + "_testContains" + contentsString.charAt(1),
					testContains(scenario.build(), contents[1], Result.True));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.False));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 2));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			// Iterator
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.True));
			printTest(scenarioName + "_testIterNext",
					testIterNext(scenario.build().iterator(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testIterRemove",
					testIterRemove(scenario.build().iterator(), Result.IllegalState));
			printTest(scenarioName + "_iterNext_testIterHasNext",
					testIterHasNext(iterAfterNext(scenario.build(), 1), Result.True));
			printTest(scenarioName + "_iterNext_testIterNext",
					testIterNext(iterAfterNext(scenario.build(), 1), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_iterNext_testIterRemove",
					testIterRemove(iterAfterNext(scenario.build(), 1), Result.NoException));
			printTest(scenarioName + "_iterNextRemove_testIterHasNext",
					testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.True));
			printTest(scenarioName + "_iterNextRemove_testIterNext", testIterNext(
					iterAfterRemove(iterAfterNext(scenario.build(), 1)), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_iterNextRemove_testIterRemove",
					testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.IllegalState));
			printTest(scenarioName + "_iterNextNext_testIterHasNext",
					testIterHasNext(iterAfterNext(scenario.build(), 2), Result.False));
			printTest(scenarioName + "_iterNextNext_testIterNext",
					testIterNext(iterAfterNext(scenario.build(), 2), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextNext_testIterRemove",
					testIterRemove(iterAfterNext(scenario.build(), 2), Result.NoException));
			printTest(scenarioName + "_iterNextNextRemove_testIterHasNext",
					testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.False));
			printTest(scenarioName + "_iterNextNextRemove_testIterNext",
					testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextNextRemove_testIterRemove",
					testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.IllegalState));
			printTest(scenarioName + "_iterNextRemoveNext_testIterHasNext",
					testIterHasNext(iterAdvance(iterAfterRemove(iterAfterNext(scenario.build(), 1)), 1), Result.False));
			printTest(scenarioName + "_iterNextRemoveNext_testIterNext", testIterNext(
					iterAdvance(iterAfterRemove(iterAfterNext(scenario.build(), 1)), 1), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextRemoveNext_testIterRemove", testIterRemove(
					iterAdvance(iterAfterRemove(iterAfterNext(scenario.build(), 1)), 1), Result.NoException));
			if (SUPPORTS_LIST_ITERATOR) {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
				printTest(scenarioName + "_testListIterNeg1",
						testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
				printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.NoException));
				printTest(scenarioName + "_testListIter2", testListIter(scenario.build(), 2, Result.NoException));
				printTest(scenarioName + "_testListIter3", testListIter(scenario.build(), 3, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIterHasNext",
						testIterHasNext(scenario.build().listIterator(), Result.True));
				printTest(scenarioName + "_testListIterHasNext",
						testIterHasNext(scenario.build().listIterator(0), Result.True));
				printTest(scenarioName + "_testListIterHasNext1",
						testIterHasNext(scenario.build().listIterator(1), Result.True));
				printTest(scenarioName + "_testListIterHasNext2",
						testIterHasNext(scenario.build().listIterator(2), Result.False));
				printTest(scenarioName + "_testListIterNext",
						testIterNext(scenario.build().listIterator(), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIterNext0",
						testIterNext(scenario.build().listIterator(0), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIterNext1",
						testIterNext(scenario.build().listIterator(1), contents[1], Result.MatchingValue));
				printTest(scenarioName + "_testListIterNext2",
						testIterNext(scenario.build().listIterator(2), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIterNextIndex",
						testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIterNextIndex1",
						testListIterNextIndex(scenario.build().listIterator(1), 1, Result.MatchingValue));
				printTest(scenarioName + "_testListIterNextIndex2",
						testListIterNextIndex(scenario.build().listIterator(2), 2, Result.MatchingValue));
				printTest(scenarioName + "_testListIterHasPrevious",
						testListIterHasPrevious(scenario.build().listIterator(), Result.False));
				printTest(scenarioName + "_testListIterHasPrevious1",
						testListIterHasPrevious(scenario.build().listIterator(1), Result.True));
				printTest(scenarioName + "_testListIterHasPrevious2",
						testListIterHasPrevious(scenario.build().listIterator(2), Result.True));
				printTest(scenarioName + "_testListIterPrevious",
						testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIterPrevious1",
						testListIterPrevious(scenario.build().listIterator(1), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIterPrevious2",
						testListIterPrevious(scenario.build().listIterator(2), contents[1], Result.MatchingValue));
				printTest(scenarioName + "_testListIterPreviousIndex",
						testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
				printTest(scenarioName + "_testListIterPreviousIndex1",
						testListIterPreviousIndex(scenario.build().listIterator(1), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIterPreviousIndex2",
						testListIterPreviousIndex(scenario.build().listIterator(2), 1, Result.MatchingValue));
				printTest(scenarioName + "_testListIterRemove",
						testIterRemove(scenario.build().listIterator(), Result.IllegalState));
				printTest(scenarioName + "_testListIterAdd",
						testListIterAdd(scenario.build().listIterator(), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterAdd1",
						testListIterAdd(scenario.build().listIterator(1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterAdd2",
						testListIterAdd(scenario.build().listIterator(2), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterSet",
						testListIterSet(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIterNextRemove",
						testIterRemove(listIterAfterNext(scenario.build().listIterator(), 1), Result.NoException));
				printTest(scenarioName + "_testListIterNext2Remove",
						testIterRemove(listIterAfterNext(scenario.build().listIterator(), 2), Result.NoException));
				printTest(scenarioName + "_testListIterNextAdd", testListIterAdd(
						listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNext2Add", testListIterAdd(
						listIterAfterNext(scenario.build().listIterator(), 2), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1NextAdd", testListIterAdd(
						listIterAfterNext(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNextSet", testListIterSet(
						listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNext2Set", testListIterSet(
						listIterAfterNext(scenario.build().listIterator(), 2), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNextSet", testListIterSet(
						listIterAfterNext(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNextRemoveRemove",
						testIterRemove(listIterAfterRemove(listIterAfterNext(scenario.build().listIterator(), 1)),
								Result.IllegalState));
				printTest(scenarioName + "_testListIterNextPreviousRemove",
						testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1),
								Result.NoException));
				printTest(scenarioName + "_testListIterNext2PreviousRemove",
						testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 1),
								Result.NoException));
				printTest(scenarioName + "_testListIterNextPreviousRemoveRemove", testIterRemove(
						listIterAfterRemove(
								listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1)),
						Result.IllegalState));
				printTest(scenarioName + "_testListIterNextPreviousAdd",
						testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNext2PreviousAdd",
						testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNextPreviousSet",
						testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNext2PreviousSet",
						testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0HasNext",
						testIterHasNext(scenario.build().listIterator(0), Result.True));
				printTest(scenarioName + "_testListIter0Next",
						testIterNext(scenario.build().listIterator(0), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIter0NextIndex",
						testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0HasPrevious",
						testListIterHasPrevious(scenario.build().listIterator(0), Result.False));
				printTest(scenarioName + "_testListIter0Previous",
						testListIterPrevious(scenario.build().listIterator(0), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIter0PreviousIndex",
						testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0Remove",
						testIterRemove(scenario.build().listIterator(0), Result.IllegalState));
				printTest(scenarioName + "_testListIter0Add",
						testListIterAdd(scenario.build().listIterator(0), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0Set",
						testListIterSet(scenario.build().listIterator(0), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIter0NextRemove",
						testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 1), Result.NoException));
				printTest(scenarioName + "_testListIter0NextAdd", testListIterAdd(
						listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0NextNextAdd", testListIterAdd(
						listIterAfterNext(scenario.build().listIterator(0), 2), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0NextNextRemove",
						testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 2), Result.NoException));
				printTest(scenarioName + "_testListIter0NextNextSet", testListIterSet(
						listIterAfterNext(scenario.build().listIterator(0), 2), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0NextSet", testListIterSet(
						listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0NextPreviousRemove",
						testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1),
								Result.NoException));
				printTest(scenarioName + "_testListIter0NextPreviousAdd",
						testListIterAdd(
								listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0NextPreviousSet",
						testListIterSet(
								listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1HasNext",
						testIterHasNext(scenario.build().listIterator(1), Result.True));
				printTest(scenarioName + "_testListIter2HasNext",
						testIterHasNext(scenario.build().listIterator(2), Result.False));

				printTest(scenarioName + "_testListIter1Next",
						testIterNext(scenario.build().listIterator(1), contents[1], Result.MatchingValue));
				printTest(scenarioName + "_testListIter1NextIndex",
						testListIterNextIndex(scenario.build().listIterator(1), 1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter1HasPrevious",
						testListIterHasPrevious(scenario.build().listIterator(1), Result.True));
				printTest(scenarioName + "_testListIter1Previous",
						testListIterPrevious(scenario.build().listIterator(1), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIter1PreviousIndex",
						testListIterPreviousIndex(scenario.build().listIterator(1), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIter1Remove",
						testIterRemove(scenario.build().listIterator(1), Result.IllegalState));
				printTest(scenarioName + "_testListIter1Add",
						testListIterAdd(scenario.build().listIterator(1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1Set",
						testListIterSet(scenario.build().listIterator(1), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIter1PreviousRemove",
						testIterRemove(listIterAfterPrevious(scenario.build().listIterator(1), 1), Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousAdd", testListIterAdd(
						listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousSet", testListIterSet(
						listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousNextRemove",
						testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1),
								Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousNextAdd",
						testListIterAdd(
								listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousNextSet",
						testListIterSet(
								listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter2HasNext",
						testIterHasNext(scenario.build().listIterator(2), Result.False));
				printTest(scenarioName + "_testListIter2Next",
						testIterNext(scenario.build().listIterator(2), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIter2NextIndex",
						testListIterNextIndex(scenario.build().listIterator(2), 2, Result.MatchingValue));
				printTest(scenarioName + "_testListIter2HasPrevious",
						testListIterHasPrevious(scenario.build().listIterator(2), Result.True));
				printTest(scenarioName + "_testListIter2Previous",
						testListIterPrevious(scenario.build().listIterator(2), contents[1], Result.MatchingValue));
				printTest(scenarioName + "_testListIter2PreviousIndex",
						testListIterPreviousIndex(scenario.build().listIterator(2), 1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter2Remove",
						testIterRemove(scenario.build().listIterator(2), Result.IllegalState));
				printTest(scenarioName + "_testListIter2Add",
						testListIterAdd(scenario.build().listIterator(2), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter2Set",
						testListIterSet(scenario.build().listIterator(2), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIter2PreviousRemove",
						testIterRemove(listIterAfterPrevious(scenario.build().listIterator(2), 1), Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousAdd", testListIterAdd(
						listIterAfterPrevious(scenario.build().listIterator(2), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousSet", testListIterSet(
						listIterAfterPrevious(scenario.build().listIterator(2), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousNextRemove",
						testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1),
								Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousNextAdd",
						testListIterAdd(
								listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousNextSet",
						testListIterSet(
								listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1),
								ELEMENT_X, Result.NoException));
			} else {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.UnsupportedOperation));
				printTest(scenarioName + "_testListIter0",
						testListIter(scenario.build(), 0, Result.UnsupportedOperation));
			}

		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	//////////////////////////////////
	// XXX Tests for 3-element list
	//////////////////////////////////

	/**
	 * Run all tests on scenarios resulting in a three-element list
	 * 
	 * @param scenario       lambda reference to scenario builder method
	 * @param scenarioName   name of the scenario being tested
	 * @param contents       elements expected in the list after scenario has been
	 *                       set up
	 * @param contentsString contains character labels corresponding to values in
	 *                       contents
	 */
	private void testThreeElementList(Scenario<Integer> scenario, String scenarioName, Integer[] contents,
			String contentsString) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			printTest(scenarioName + "_testAddToFront",
					testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter0" + contentsString.charAt(0),
					testAddAfter(scenario.build(), contents[0], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter1" + contentsString.charAt(1),
					testAddAfter(scenario.build(), contents[1], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter2" + contentsString.charAt(2),
					testAddAfter(scenario.build(), contents[2], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX",
					testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAdd", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex0",
					testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1",
					testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex2",
					testAddAtIndex(scenario.build(), 2, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex3",
					testAddAtIndex(scenario.build(), 3, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex4",
					testAddAtIndex(scenario.build(), 4, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex-1",
					testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemoveFirst",
					testRemoveFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveLast",
					testRemoveLast(scenario.build(), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveAt0" + contentsString.charAt(0),
					testRemoveElement(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveAt2" + contentsString.charAt(2),
					testRemoveElement(scenario.build(), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveX",
					testRemoveElement(scenario.build(), ELEMENT_X, Result.NoSuchElement));
			printTest(scenarioName + "_testRemoveA",
					testRemoveElement(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testSetNeg", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet1", testSet(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet2", testSet(scenario.build(), 2, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSetX", testSet(scenario.build(), 3, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testGet1", testGet(scenario.build(), 1, contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testGet2", testGet(scenario.build(), 2, contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testGet3", testGet(scenario.build(), 3, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOf0" + contentsString.charAt(0),
					testIndexOf(scenario.build(), contents[0], 0));
			printTest(scenarioName + "_testIndexOf2" + contentsString.charAt(2),
					testIndexOf(scenario.build(), contents[2], 2));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testContains" + contentsString.charAt(0),
					testContains(scenario.build(), contents[0], Result.True));
			printTest(scenarioName + "_testContains" + contentsString.charAt(2),
					testContains(scenario.build(), contents[2], Result.True));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.False));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 3));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			// Iterator
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.True));
			printTest(scenarioName + "_testIterNext",
					testIterNext(scenario.build().iterator(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testIterRemove",
					testIterRemove(scenario.build().iterator(), Result.IllegalState));
			printTest(scenarioName + "_iterNext_testIterHasNext",
					testIterHasNext(iterAfterNext(scenario.build(), 1), Result.True));
			printTest(scenarioName + "_iterNext_testIterNext",
					testIterNext(iterAfterNext(scenario.build(), 1), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_iterNext_testIterRemove",
					testIterRemove(iterAfterNext(scenario.build(), 1), Result.NoException));
			printTest(scenarioName + "_iterNextRemove_testIterHasNext",
					testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.True));
			printTest(scenarioName + "_iterNextRemove_testIterNext", testIterNext(
					iterAfterRemove(iterAfterNext(scenario.build(), 1)), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_iterNextRemove_testIterRemove",
					testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.IllegalState));
			printTest(scenarioName + "_iterNextRemoveNext_testIterHasNext",
					testIterHasNext(iterNextRNext(scenario.build().iterator()), Result.True));
			printTest(scenarioName + "_iterNextRemoveNext_testIterNext",
					testIterNext(iterNextRNext(scenario.build().iterator()), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_iterNextRemoveNext_testIterRemove",
					testIterRemove(iterNextRNext(scenario.build().iterator()), Result.NoException));
			printTest(scenarioName + "_iterNextNext_testIterHasNext",
					testIterHasNext(iterAfterNext(scenario.build(), 2), Result.True));
			printTest(scenarioName + "_iterNextNext_testIterNext",
					testIterNext(iterAfterNext(scenario.build(), 2), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_iterNextNext_testIterRemove",
					testIterRemove(iterAfterNext(scenario.build(), 2), Result.NoException));
			printTest(scenarioName + "_iterNextNextRemove_testIterHasNext",
					testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.True));
			printTest(scenarioName + "_iterNextNextRemove_testIterNext", testIterNext(
					iterAfterRemove(iterAfterNext(scenario.build(), 2)), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_iterNextNextRemove_testIterRemove",
					testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.IllegalState));
			printTest(scenarioName + "_iterNextNextRemoveNext_testIterHasNext",
					testIterHasNext(iterNextNextRNext(scenario.build().iterator()), Result.False));
			printTest(scenarioName + "_iterNextNextRemoveNext_testIterNext",
					testIterNext(iterNextNextRNext(scenario.build().iterator()), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextNextRemoveNext_testIterRemove",
					testIterRemove(iterNextNextRNext(scenario.build().iterator()), Result.NoException));
			printTest(scenarioName + "_iterNextNextNext_testIterHasNext",
					testIterHasNext(iterAfterNext(scenario.build(), 3), Result.False));
			printTest(scenarioName + "_iterNextNextNext_testIterNext",
					testIterNext(iterAfterNext(scenario.build(), 3), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextNextNext_testIterRemove",
					testIterRemove(iterAfterNext(scenario.build(), 3), Result.NoException));
			printTest(scenarioName + "_iterNextNextNextRemove_testIterHasNext",
					testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 3)), Result.False));
			printTest(scenarioName + "_iterNextNextNextRemove_testIterNext",
					testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 3)), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextNextNextRemove_testIterRemove",
					testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 3)), Result.IllegalState));
			if (SUPPORTS_LIST_ITERATOR) {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
				printTest(scenarioName + "_testListIterNeg1",
						testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
				printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.NoException));
				printTest(scenarioName + "_testListIter2", testListIter(scenario.build(), 2, Result.NoException));
				printTest(scenarioName + "_testListIter3", testListIter(scenario.build(), 3, Result.NoException));
				printTest(scenarioName + "_testListIter4", testListIter(scenario.build(), 4, Result.IndexOutOfBounds));
				printTest(scenarioName + "_testListIterHasNext",
						testIterHasNext(scenario.build().listIterator(), Result.True));
				printTest(scenarioName + "_testListIterHasNext",
						testIterHasNext(scenario.build().listIterator(0), Result.True));
				printTest(scenarioName + "_testListIterHasNext1",
						testIterHasNext(scenario.build().listIterator(1), Result.True));
				printTest(scenarioName + "_testListIterHasNext2",
						testIterHasNext(scenario.build().listIterator(2), Result.True));
				printTest(scenarioName + "_testListIterHasNext3",
						testIterHasNext(scenario.build().listIterator(3), Result.False));
				printTest(scenarioName + "_testListIterNext",
						testIterNext(scenario.build().listIterator(), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIterNext0",
						testIterNext(scenario.build().listIterator(0), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIterNext1",
						testIterNext(scenario.build().listIterator(1), contents[1], Result.MatchingValue));
				printTest(scenarioName + "_testListIterNext2",
						testIterNext(scenario.build().listIterator(2), contents[2], Result.MatchingValue));
				printTest(scenarioName + "_testListIterNext3",
						testIterNext(scenario.build().listIterator(3), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIterNextIndex",
						testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIterNextIndex1",
						testListIterNextIndex(scenario.build().listIterator(1), 1, Result.MatchingValue));
				printTest(scenarioName + "_testListIterNextIndex2",
						testListIterNextIndex(scenario.build().listIterator(2), 2, Result.MatchingValue));
				printTest(scenarioName + "_testListIterNextIndex3",
						testListIterNextIndex(scenario.build().listIterator(3), 3, Result.MatchingValue));
				printTest(scenarioName + "_testListIterHasPrevious",
						testListIterHasPrevious(scenario.build().listIterator(), Result.False));
				printTest(scenarioName + "_testListIterHasPrevious1",
						testListIterHasPrevious(scenario.build().listIterator(1), Result.True));
				printTest(scenarioName + "_testListIterHasPrevious2",
						testListIterHasPrevious(scenario.build().listIterator(2), Result.True));
				printTest(scenarioName + "_testListIterHasPrevious3",
						testListIterHasPrevious(scenario.build().listIterator(3), Result.True));

				printTest(scenarioName + "_testListIterPrevious",
						testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIterPrevious1",
						testListIterPrevious(scenario.build().listIterator(1), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIterPrevious2",
						testListIterPrevious(scenario.build().listIterator(2), contents[1], Result.MatchingValue));
				printTest(scenarioName + "_testListIterPrevious3",
						testListIterPrevious(scenario.build().listIterator(3), contents[2], Result.MatchingValue));

				printTest(scenarioName + "_testListIterPreviousIndex",
						testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
				printTest(scenarioName + "_testListIterPreviousIndex1",
						testListIterPreviousIndex(scenario.build().listIterator(1), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIterPreviousIndex2",
						testListIterPreviousIndex(scenario.build().listIterator(2), 1, Result.MatchingValue));
				printTest(scenarioName + "_testListIterPreviousIndex3",
						testListIterPreviousIndex(scenario.build().listIterator(3), 2, Result.MatchingValue));

				printTest(scenarioName + "_testListIterRemove",
						testIterRemove(scenario.build().listIterator(), Result.IllegalState));
				printTest(scenarioName + "_testListIterAdd",
						testListIterAdd(scenario.build().listIterator(), ELEMENT_X, Result.NoException));

				printTest(scenarioName + "_testListIterAdd1",
						testListIterAdd(scenario.build().listIterator(1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterAdd2",
						testListIterAdd(scenario.build().listIterator(2), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterAdd3",
						testListIterAdd(scenario.build().listIterator(3), ELEMENT_X, Result.NoException));

				printTest(scenarioName + "_testListIterSet",
						testListIterSet(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIterNextRemove",
						testIterRemove(listIterAfterNext(scenario.build().listIterator(), 1), Result.NoException));
				printTest(scenarioName + "_testListIterNext2Remove",
						testIterRemove(listIterAfterNext(scenario.build().listIterator(), 2), Result.NoException));
				printTest(scenarioName + "_testListIterNext3Remove",
						testIterRemove(listIterAfterNext(scenario.build().listIterator(), 3), Result.NoException));

				printTest(scenarioName + "_testListIterNextAdd", testListIterAdd(
						listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNext2Add", testListIterAdd(
						listIterAfterNext(scenario.build().listIterator(), 2), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNext3Add", testListIterAdd(
						listIterAfterNext(scenario.build().listIterator(), 3), ELEMENT_X, Result.NoException));

				printTest(scenarioName + "_testListIter1NextAdd", testListIterAdd(
						listIterAfterNext(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNextSet", testListIterSet(
						listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNext2Set", testListIterSet(
						listIterAfterNext(scenario.build().listIterator(), 2), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNext3Set", testListIterSet(
						listIterAfterNext(scenario.build().listIterator(), 3), ELEMENT_X, Result.NoException));

				printTest(scenarioName + "_testListIterNextSet", testListIterSet(
						listIterAfterNext(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNextRemoveRemove",
						testIterRemove(listIterAfterRemove(listIterAfterNext(scenario.build().listIterator(), 1)),
								Result.IllegalState));
				printTest(scenarioName + "_testListIterNextPreviousRemove",
						testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1),
								Result.NoException));
				printTest(scenarioName + "_testListIterNext2PreviousRemove",
						testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 1),
								Result.NoException));
				printTest(scenarioName + "_testListIterNext3PreviousRemove",
						testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 3), 1),
								Result.NoException));

				printTest(scenarioName + "_testListIterNextPreviousRemoveRemove", testIterRemove(
						listIterAfterRemove(
								listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1)),
						Result.IllegalState));
				printTest(scenarioName + "_testListIterNextPreviousAdd",
						testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNext2PreviousAdd",
						testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNext3PreviousAdd",
						testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 3), 1),
								ELEMENT_X, Result.NoException));

				printTest(scenarioName + "_testListIterNextPreviousSet",
						testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNext2PreviousSet",
						testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 2), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIterNext3PreviousSet",
						testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 3), 1),
								ELEMENT_X, Result.NoException));

				printTest(scenarioName + "_testListIter0HasNext",
						testIterHasNext(scenario.build().listIterator(0), Result.True));
				printTest(scenarioName + "_testListIter0Next",
						testIterNext(scenario.build().listIterator(0), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIter0NextIndex",
						testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0HasPrevious",
						testListIterHasPrevious(scenario.build().listIterator(0), Result.False));
				printTest(scenarioName + "_testListIter0Previous",
						testListIterPrevious(scenario.build().listIterator(0), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIter0PreviousIndex",
						testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter0Remove",
						testIterRemove(scenario.build().listIterator(0), Result.IllegalState));
				printTest(scenarioName + "_testListIter0Add",
						testListIterAdd(scenario.build().listIterator(0), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0Set",
						testListIterSet(scenario.build().listIterator(0), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIter0NextRemove",
						testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 1), Result.NoException));
				printTest(scenarioName + "_testListIter0NextAdd", testListIterAdd(
						listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0NextNextAdd", testListIterAdd(
						listIterAfterNext(scenario.build().listIterator(0), 2), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0NextNextRemove",
						testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 2), Result.NoException));
				printTest(scenarioName + "_testListIter0NextNextSet", testListIterSet(
						listIterAfterNext(scenario.build().listIterator(0), 2), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0NextSet", testListIterSet(
						listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0NextPreviousRemove",
						testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1),
								Result.NoException));
				printTest(scenarioName + "_testListIter0NextPreviousAdd",
						testListIterAdd(
								listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter0NextPreviousSet",
						testListIterSet(
								listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1HasNext",
						testIterHasNext(scenario.build().listIterator(1), Result.True));
				printTest(scenarioName + "_testListIter2HasNext",
						testIterHasNext(scenario.build().listIterator(2), Result.True));
				printTest(scenarioName + "_testListIter3HasNext",
						testIterHasNext(scenario.build().listIterator(3), Result.False));

				printTest(scenarioName + "_testListIter1Next",
						testIterNext(scenario.build().listIterator(1), contents[1], Result.MatchingValue));
				printTest(scenarioName + "_testListIter1NextIndex",
						testListIterNextIndex(scenario.build().listIterator(1), 1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter1HasPrevious",
						testListIterHasPrevious(scenario.build().listIterator(1), Result.True));
				printTest(scenarioName + "_testListIter1Previous",
						testListIterPrevious(scenario.build().listIterator(1), contents[0], Result.MatchingValue));
				printTest(scenarioName + "_testListIter1PreviousIndex",
						testListIterPreviousIndex(scenario.build().listIterator(1), 0, Result.MatchingValue));
				printTest(scenarioName + "_testListIter1Remove",
						testIterRemove(scenario.build().listIterator(1), Result.IllegalState));
				printTest(scenarioName + "_testListIter1Add",
						testListIterAdd(scenario.build().listIterator(1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1Set",
						testListIterSet(scenario.build().listIterator(1), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIter1PreviousRemove",
						testIterRemove(listIterAfterPrevious(scenario.build().listIterator(1), 1), Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousAdd", testListIterAdd(
						listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousSet", testListIterSet(
						listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousNextRemove",
						testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1),
								Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousNextAdd",
						testListIterAdd(
								listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter1PreviousNextSet",
						testListIterSet(
								listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter2Next",
						testIterNext(scenario.build().listIterator(2), contents[2], Result.MatchingValue));

				printTest(scenarioName + "_testListIter2NextIndex",
						testListIterNextIndex(scenario.build().listIterator(2), 2, Result.MatchingValue));
				printTest(scenarioName + "_testListIter2HasPrevious",
						testListIterHasPrevious(scenario.build().listIterator(2), Result.True));
				printTest(scenarioName + "_testListIter2Previous",
						testListIterPrevious(scenario.build().listIterator(2), contents[1], Result.MatchingValue));
				printTest(scenarioName + "_testListIter2PreviousIndex",
						testListIterPreviousIndex(scenario.build().listIterator(2), 1, Result.MatchingValue));
				printTest(scenarioName + "_testListIter2Remove",
						testIterRemove(scenario.build().listIterator(2), Result.IllegalState));
				printTest(scenarioName + "_testListIter2Add",
						testListIterAdd(scenario.build().listIterator(2), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter2Set",
						testListIterSet(scenario.build().listIterator(2), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIter2PreviousRemove",
						testIterRemove(listIterAfterPrevious(scenario.build().listIterator(2), 1), Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousAdd", testListIterAdd(
						listIterAfterPrevious(scenario.build().listIterator(2), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousSet", testListIterSet(
						listIterAfterPrevious(scenario.build().listIterator(2), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousNextRemove",
						testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1),
								Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousNextAdd",
						testListIterAdd(
								listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter2PreviousNextSet",
						testListIterSet(
								listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1),
								ELEMENT_X, Result.NoException));

				printTest(scenarioName + "_testListIter3HasNext",
						testIterHasNext(scenario.build().listIterator(3), Result.False));
				printTest(scenarioName + "_testListIter3Next",
						testIterNext(scenario.build().listIterator(3), null, Result.NoSuchElement));
				printTest(scenarioName + "_testListIter3NextIndex",
						testListIterNextIndex(scenario.build().listIterator(3), 3, Result.MatchingValue));
				printTest(scenarioName + "_testListIter3HasPrevious",
						testListIterHasPrevious(scenario.build().listIterator(3), Result.True));
				printTest(scenarioName + "_testListIter3Previous",
						testListIterPrevious(scenario.build().listIterator(3), contents[2], Result.MatchingValue));
				printTest(scenarioName + "_testListIter3PreviousIndex",
						testListIterPreviousIndex(scenario.build().listIterator(3), 2, Result.MatchingValue));
				printTest(scenarioName + "_testListIter3Remove",
						testIterRemove(scenario.build().listIterator(3), Result.IllegalState));
				printTest(scenarioName + "_testListIter3Add",
						testListIterAdd(scenario.build().listIterator(3), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter3Set",
						testListIterSet(scenario.build().listIterator(3), ELEMENT_X, Result.IllegalState));
				printTest(scenarioName + "_testListIter3PreviousRemove",
						testIterRemove(listIterAfterPrevious(scenario.build().listIterator(3), 1), Result.NoException));
				printTest(scenarioName + "_testListIter3Previous2Remove",
						testIterRemove(listIterAfterPrevious(scenario.build().listIterator(3), 2), Result.NoException));
				printTest(scenarioName + "_testListIter3Previous3Remove",
						testIterRemove(listIterAfterPrevious(scenario.build().listIterator(3), 3), Result.NoException));

				printTest(scenarioName + "_testListIter3PreviousAdd", testListIterAdd(
						listIterAfterPrevious(scenario.build().listIterator(3), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter3Previous2Add", testListIterAdd(
						listIterAfterPrevious(scenario.build().listIterator(3), 2), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter3Previous3Add", testListIterAdd(
						listIterAfterPrevious(scenario.build().listIterator(3), 3), ELEMENT_X, Result.NoException));

				printTest(scenarioName + "_testListIter3PreviousSet", testListIterSet(
						listIterAfterPrevious(scenario.build().listIterator(3), 1), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter3Previous2Set", testListIterSet(
						listIterAfterPrevious(scenario.build().listIterator(3), 2), ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter3Previous3Set", testListIterSet(
						listIterAfterPrevious(scenario.build().listIterator(3), 3), ELEMENT_X, Result.NoException));

				printTest(scenarioName + "_testListIter3PreviousNextRemove",
						testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 1), 1),
								Result.NoException));
				printTest(scenarioName + "_testListIter3PreviousNextAdd",
						testListIterAdd(
								listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 1), 1),
								ELEMENT_X, Result.NoException));
				printTest(scenarioName + "_testListIter3PreviousNextSet",
						testListIterSet(
								listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 1), 1),
								ELEMENT_X, Result.NoException));
			} else {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.UnsupportedOperation));
				printTest(scenarioName + "_testListIter0",
						testListIter(scenario.build(), 0, Result.UnsupportedOperation));
			}
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	////////////////////////////
	// XXX LIST TEST METHODS
	////////////////////////////

	/**
	 * Runs removeFirst() method on given list and checks result against
	 * expectedResult
	 * 
	 * @param list            a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveFirst(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.removeFirst();
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveFirst", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs removeLast() method on given list and checks result against
	 * expectedResult
	 * 
	 * @param list            a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveLast(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.removeLast();
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveLast", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs removeLast() method on given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param element        element to remove
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveElement(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.remove(element);
			if (retVal.equals(element)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveElement", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs first() method on a given list and checks result against expectedResult
	 * 
	 * @param list            a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testFirst(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.first();
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testFirst", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs last() method on a given list and checks result against expectedResult
	 * 
	 * @param list            a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testLast(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.last();
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testLast", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs contains() method on a given list and element and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testContains(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			if (list.contains(element)) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testContains", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs isEmpty() method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIsEmpty(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			if (list.isEmpty()) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIsEmpty", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs size() method on a given list and checks result against expectedResult
	 * 
	 * @param list         a list already prepared for a given change scenario
	 * @param expectedSize
	 * @return test success
	 */
	private boolean testSize(IndexedUnsortedList<Integer> list, int expectedSize) {
		try {
			return (list.size() == expectedSize);
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testSize", e.toString());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Runs toString() method on given list and attempts to confirm non-default or
	 * empty String
	 * difficult to test - just confirm that default address output has been
	 * overridden
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testToString(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			String str = list.toString().trim();
			if (showToString) {
				System.out.println("toString() output: " + str);
			}
			if (str.length() < (list.size() + list.size() / 2 + 2)) { // elements + commas + '[' + ']'
				result = Result.Fail;
			} else {
				char lastChar = str.charAt(str.length() - 1);
				char firstChar = str.charAt(0);
				if (firstChar != '[' || lastChar != ']') {
					result = Result.Fail;
				} else if (str.contains("@")
						&& !str.contains(" ")
						&& Character.isLetter(str.charAt(0))
						&& (Character.isDigit(lastChar) || (lastChar >= 'a' && lastChar <= 'f'))) {
					result = Result.Fail; // looks like default toString()
				} else {
					result = Result.ValidString;
				}
			}
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testToString", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs addToFront() method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddToFront(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			list.addToFront(element);
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddToFront", e.toString());
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs addToRear() method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddToRear(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			list.addToRear(element);
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddToRear", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs addAfter() method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param target
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddAfter(IndexedUnsortedList<Integer> list, Integer target, Integer element,
			Result expectedResult) {
		Result result;
		try {
			list.addAfter(element, target);
			result = Result.NoException;
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddAfter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs add(int, T) method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param index
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddAtIndex(IndexedUnsortedList<Integer> list, int index, Integer element,
			Result expectedResult) {
		Result result;
		try {
			list.add(index, element);
			result = Result.NoException;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddAtIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs add(T) method on a given list and checks result against expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAdd(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			list.add(element);
			result = Result.NoException;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddAtIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs set(int, T) method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param index
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testSet(IndexedUnsortedList<Integer> list, int index, Integer element, Result expectedResult) {
		Result result;
		try {
			list.set(index, element);
			result = Result.NoException;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testSet", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs get() method on a given list and checks result against expectedResult
	 * 
	 * @param list            a list already prepared for a given change scenario
	 * @param index
	 * @param expectedElement
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testGet(IndexedUnsortedList<Integer> list, int index, Integer expectedElement,
			Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.get(index);
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testGet", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs remove(index) method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list            a list already prepared for a given change scenario
	 * @param index
	 * @param expectedElement
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveIndex(IndexedUnsortedList<Integer> list, int index, Integer expectedElement,
			Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.remove(index);
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs indexOf() method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list          a list already prepared for a given change scenario
	 * @param element
	 * @param expectedIndex
	 * @return test success
	 */
	private boolean testIndexOf(IndexedUnsortedList<Integer> list, Integer element, int expectedIndex) {
		try {
			return list.indexOf(element) == expectedIndex;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIndexOf", e.toString());
			e.printStackTrace();
			return false;
		}
	}

	////////////////////////////
	// XXX ITERATOR TESTS
	////////////////////////////

	/**
	 * Custom helper method, runs next, remove, next
	 * 
	 * @param it the iterator to advance
	 * @return
	 */
	private Iterator<Integer> iterNextRNext(Iterator<Integer> it) {
		it.next();
		it.remove();
		it.next();
		return it;
	}

	/**
	 * Custom helper method, runs next, next, remove, next
	 * 
	 * @param it the iterator to advance
	 * @return
	 */
	private Iterator<Integer> iterNextNextRNext(Iterator<Integer> it) {
		it.next();
		it.next();
		it.remove();
		it.next();
		return it;
	}

	/**
	 * Advances an existing iterator a given number of times.
	 * 
	 * @param it       the iterator that gets advanced
	 * @param numCalls the number of times to call next()
	 * @return the same iterator after the advancing
	 */
	private Iterator<Integer> iterAdvance(Iterator<Integer> it, int numCalls) {
		for (int i = 0; i < numCalls; i++) {
			it.next();
		}
		return it;
	}

	/**
	 * Runs iterator() method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIter(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it = list.iterator();
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs list's iterator hasNext() method and checks result against
	 * expectedResult
	 * 
	 * @param iterator       an iterator already positioned for the call to
	 *                       hasNext()
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIterHasNext(Iterator<Integer> iterator, Result expectedResult) {
		Result result;
		try {
			if (iterator.hasNext()) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterHasNext", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs list's iterator next() method and checks result against expectedResult
	 * 
	 * @param iterator       an iterator already positioned for the call to
	 *                       hasNext()
	 * @param expectedValue  the Integer expected from next() or null if an
	 *                       exception is expected
	 * @param expectedResult MatchingValue or expected exception
	 * @return test success
	 */
	private boolean testIterNext(Iterator<Integer> iterator, Integer expectedValue, Result expectedResult) {
		Result result;
		try {
			Integer retVal = iterator.next();
			if (retVal.equals(expectedValue)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterNext", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs list's iterator remove() method and checks result against expectedResult
	 * 
	 * @param iterator       an iterator already positioned for the call to remove()
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIterRemove(Iterator<Integer> iterator, Result expectedResult) {
		Result result;
		try {
			iterator.remove();
			result = Result.NoException;
		} catch (IllegalStateException e) {
			result = Result.IllegalState;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterRemove", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs iterator() method twice on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIterConcurrent(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it1 = list.iterator();
			@SuppressWarnings("unused")
			Iterator<Integer> it2 = list.iterator();
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterConcurrent", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	//////////////////////////////////////////////////////////
	// XXX HELPER METHODS FOR TESTING ITERATORS
	// Note: You can create other similar helpers if you want
	// something slightly different.
	//////////////////////////////////////////////////////////

	/**
	 * Helper for testing iterators. Return an Iterator that has been advanced
	 * numCallsToNext times.
	 * 
	 * @param list
	 * @param numCallsToNext
	 * @return Iterator for given list, after numCallsToNext
	 */
	private Iterator<Integer> iterAfterNext(IndexedUnsortedList<Integer> list, int numCallsToNext) {
		Iterator<Integer> it = list.iterator();
		for (int i = 0; i < numCallsToNext; i++) {
			it.next();
		}
		return it;
	}

	/**
	 * Helper for testing iterators. Return an Iterator that has had remove() called
	 * once.
	 * 
	 * @param iterator
	 * @return same Iterator following a call to remove()
	 */
	private Iterator<Integer> iterAfterRemove(Iterator<Integer> iterator) {
		iterator.remove();
		return iterator;
	}

	////////////////////////////////////////////////////////////////////////
	// XXX LISTITERATOR TESTS
	// Note: can use Iterator tests for hasNext(), next(), and remove()
	////////////////////////////////////////////////////////////////////////

	/**
	 * Runs listIterator() method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIter(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it = list.listIterator();
			result = Result.NoException;
		} catch (UnsupportedOperationException e) {
			result = Result.UnsupportedOperation;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs listIterator(index) method on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param expectedResult
	 * @param startingIndex
	 * @return test success
	 */
	private boolean testListIter(IndexedUnsortedList<Integer> list, int startingIndex, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it = list.listIterator(startingIndex);
			result = Result.NoException;
		} catch (UnsupportedOperationException e) {
			result = Result.UnsupportedOperation;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs ListIterator's hasPrevious() method and checks result against
	 * expectedResult
	 * 
	 * @param iterator       an iterator already positioned for the call to
	 *                       hasPrevious()
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterHasPrevious(ListIterator<Integer> iterator, Result expectedResult) {
		Result result;
		try {
			if (iterator.hasPrevious()) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterHasPrevious", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs ListIterator previous() method and checks result against expectedResult
	 * 
	 * @param iterator       an iterator already positioned for the call to
	 *                       hasPrevious()
	 * @param expectedValue  the Integer expected from next() or null if an
	 *                       exception is expected
	 * @param expectedResult MatchingValue or expected exception
	 * @return test success
	 */
	private boolean testListIterPrevious(ListIterator<Integer> iterator, Integer expectedValue, Result expectedResult) {
		Result result;
		try {
			Integer retVal = iterator.previous();
			if (retVal.equals(expectedValue)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterPrevious", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs ListIterator add() method and checks result against expectedResult
	 * 
	 * @param iterator       an iterator already positioned for the call to add()
	 * @param element        new Integer for insertion
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterAdd(ListIterator<Integer> iterator, Integer element, Result expectedResult) {
		Result result;
		try {
			iterator.add(element);
			result = Result.NoException;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterAdd", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs ListIterator set() method and checks result against expectedResult
	 * 
	 * @param iterator       an iterator already positioned for the call to set()
	 * @param element        replacement Integer for last returned element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterSet(ListIterator<Integer> iterator, Integer element, Result expectedResult) {
		Result result;
		try {
			iterator.set(element);
			result = Result.NoException;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (IllegalStateException e) {
			result = Result.IllegalState;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterSet", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs ListIterator nextIndex() and checks result against expected Result
	 * 
	 * @param iterator       already positioned for the call to nextIndex()
	 * @param expectedIndex
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterNextIndex(ListIterator<Integer> iterator, int expectedIndex, Result expectedResult) {
		Result result;
		try {
			int idx = iterator.nextIndex();
			if (idx == expectedIndex) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterNextIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs ListIterator previousIndex() and checks result against expected Result
	 * 
	 * @param iterator       already positioned for the call to previousIndex()
	 * @param expectedIndex
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterPreviousIndex(ListIterator<Integer> iterator, int expectedIndex,
			Result expectedResult) {
		Result result;
		try {
			int idx = iterator.previousIndex();
			if (idx == expectedIndex) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterPreviousIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs listIterator() method twice on a given list and checks result against
	 * expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterConcurrent(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			ListIterator<Integer> it1 = list.listIterator();
			@SuppressWarnings("unused")
			ListIterator<Integer> it2 = list.listIterator();
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterConcurrent", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/**
	 * Runs listIterator(index) method twice on a given list and checks result
	 * against expectedResult
	 * 
	 * @param list           a list already prepared for a given change scenario
	 * @param index1
	 * @param index2
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterConcurrent(IndexedUnsortedList<Integer> list, int index1, int index2,
			Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			ListIterator<Integer> it1 = list.listIterator(index1);
			@SuppressWarnings("unused")
			ListIterator<Integer> it2 = list.listIterator(index2);
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterConcurrent", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	//////////////////////////////////////////////////////////
	// XXX HELPER METHODS FOR TESTING LISTITERATORS
	// Note: You can create other similar helpers if you want
	// something slightly different.
	//////////////////////////////////////////////////////////

	/**
	 * Helper for testing ListIterators. Return a ListIterator that has been
	 * advanced numCallsToNext times.
	 * 
	 * @param iterator
	 * @param numCallsToNext
	 * @return same iterator after numCallsToNext
	 */
	private ListIterator<Integer> listIterAfterNext(ListIterator<Integer> iterator, int numCallsToNext) {
		for (int i = 0; i < numCallsToNext; i++) {
			iterator.next();
		}
		return iterator;
	}

	/**
	 * Helper for testing ListIterators. Return a ListIterator that has been backed
	 * up numCallsToPrevious times.
	 * 
	 * @param iterator
	 * @param numCallsToPrevious
	 * @return same iterator after numCallsToPrevious
	 */
	private ListIterator<Integer> listIterAfterPrevious(ListIterator<Integer> iterator, int numCallsToPrevious) {
		for (int i = 0; i < numCallsToPrevious; i++) {
			iterator.previous();
		}
		return iterator;
	}

	/**
	 * Helper for testing ListIterators. Return a ListIterator that has had remove()
	 * called once.
	 * 
	 * @param iterator
	 * @return same Iterator following a call to remove()
	 */
	private ListIterator<Integer> listIterAfterRemove(ListIterator<Integer> iterator) {
		iterator.remove();
		return iterator;
	}

	////////////////////////////////////////////////////////
	// XXX Iterator Concurrency Tests
	// Can simply use as given. Don't need to add more.
	////////////////////////////////////////////////////////

	/** run Iterator concurrency tests */
	private void test_IterConcurrency() {
		System.out.println("\nIterator Concurrency Tests\n");
		try {
			printTest("emptyList_testConcurrentIter", testIterConcurrent(newList(), Result.NoException));
			IndexedUnsortedList<Integer> list = newList();
			Iterator<Integer> it1 = list.iterator();
			Iterator<Integer> it2 = list.iterator();
			it1.hasNext();
			printTest("emptyList_iter1HasNext_testIter2HasNext", testIterHasNext(it2, Result.False));
			list = newList();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("emptyList_iter1HasNext_testIter2Next", testIterNext(it2, null, Result.NoSuchElement));
			list = newList();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("emptyList_iter1HasNext_testIter2Remove", testIterRemove(it2, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("A_iter1HasNext_testIter2HasNext", testIterHasNext(it2, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("A_iter1HasNext_testIter2Next", testIterNext(it2, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("A_iter1HasNext_testIter2Remove", testIterRemove(it2, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			printTest("A_iter1Next_testIter2HasNext", testIterHasNext(it2, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			printTest("A_iter1Next_testIter2Next", testIterNext(it2, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			printTest("A_iter1Next_testIter2Remove", testIterRemove(it2, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			it1.remove();
			printTest("A_iter1NextRemove_testIter2HasNext", testIterHasNext(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			it1.remove();
			printTest("A_iter1NextRemove_testIter2Next", testIterNext(it2, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			it1.remove();
			printTest("A_iter1NextRemove_testIter2Remove", testIterRemove(it2, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeFirst();
			printTest("A_removeFirst_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeFirst();
			printTest("A_removeFirst_testIterNextConcurrent",
					testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeFirst();
			printTest("A_removeLast_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeLast();
			printTest("A_removeLast_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeLast();
			printTest("A_removeLast_testIterNextConcurrent",
					testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeLast();
			printTest("A_removeLast_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(ELEMENT_A);
			printTest("A_remove_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(ELEMENT_A);
			printTest("A_remove_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.first();
			printTest("A_first_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.first();
			printTest("A_first_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.first();
			printTest("A_first_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.last();
			printTest("A_last_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.last();
			printTest("A_last_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.last();
			printTest("A_last_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.isEmpty();
			printTest("A_isEmpty_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.isEmpty();
			printTest("A_isEmpty_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.isEmpty();
			printTest("A_isEmpty_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.size();
			printTest("A_size_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.size();
			printTest("A_size_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.size();
			printTest("A_size_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.toString();
			printTest("A_toString_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.toString();
			printTest("A_toString_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.toString();
			printTest("A_toString_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testIterNextConcurrent",
					testIterNext(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testIterNextConcurrent",
					testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testIterNextConcurrent",
					testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.get(0);
			printTest("A_get0_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.get(0);
			printTest("A_get0_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.get(0);
			printTest("A_get_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(0);
			printTest("A_remove0_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(0);
			printTest("A_remove0_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(0);
			printTest("A_remove0_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", "test_IteratorConcurrency");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	////////////////////////////////////////////////////////
	// XXX ListIterator Concurrency Tests
	// Will add tests for double-linked list
	////////////////////////////////////////////////////////

	/** run ListIterator concurrency tests */
	private void test_ListIterConcurrency() {
		System.out.println("\nListIterator Concurrency Tests\n");
		try {
			// TODO: will add for double-linked list
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", "test_ListIterConcurrency");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}
}// end class IndexedUnsortedListTester

/** Interface for builder method Lambda references used above */
interface Scenario<T> {
	IndexedUnsortedList<T> build();
}
