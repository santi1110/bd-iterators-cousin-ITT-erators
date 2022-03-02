package com.frank.addams;

import com.frank.emojis.Emogis;
import com.frank.exceptions.DataFileErrorException;
import com.frank.exceptions.InvalidMenuResponseException;
import com.frank.types.AddamsSearchCriteria;
import com.frank.types.Person;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

public class AddamsFamilyApplication {

        private static Scanner userKeyboardDevice = new Scanner(System.in);

        private static final String DISPLAY_ALL_ADDAMS_OPTION    = "Display all Addams'";
        private static final String DISPLAY_BY_NAME_OPTION       = "Find an Addams";
        private static final String CHANGE_AN_ADDAMS_NAME_OPTION = "Change an Addams name";
        private static final String REMOVE_AN_ADDAMS_OPTION      = "Remove an Addams";
        private static final String EXIT_OPTION                  = "Exit";

        private static final String[] mainMenuOptions = { DISPLAY_ALL_ADDAMS_OPTION
                                                        , DISPLAY_BY_NAME_OPTION
                                                        , CHANGE_AN_ADDAMS_NAME_OPTION
                                                        , REMOVE_AN_ADDAMS_OPTION
                                                        , EXIT_OPTION
                                                        };

        private List<Person> theAddamsFamily;

        public AddamsFamilyApplication() throws FileNotFoundException {

                theAddamsFamily = new ArrayList<>();

                loadFamilyMembersFromFile();
        }

        public void run()  {

            startOfApplicationProcessing();

            String whatTheyChose = new String("");

            boolean shouldLoop = true;

            while(shouldLoop) {
                 try {
                        whatTheyChose = displayMenuAndGetResponse();
                        System.out.println("\nYou chose: " + whatTheyChose);

                        switch (whatTheyChose) {
                                case DISPLAY_ALL_ADDAMS_OPTION: {
                                     displayAllPeople();
                                     break;
                                 }
                                 case DISPLAY_BY_NAME_OPTION: {
                                      displayByName();
                                      break;
                                 }
                                 case CHANGE_AN_ADDAMS_NAME_OPTION: {
                                      changePersonName();
                                      break;
                                 }
                                case REMOVE_AN_ADDAMS_OPTION: {
                                        removeAnAddams();
                                        break;
                                }
                                 case EXIT_OPTION: {
                                      shouldLoop = false;
                                      break;
                                 }
                                 default: {
                                         throw new InvalidMenuResponseException("Invalid menu option " + whatTheyChose + " entered: ");
                                 }
                         }
                 }
                 catch(InvalidMenuResponseException exceptionObject) {
                         System.out.println("\nUh-Oh, Looks like you entered an invalid response, please try again");
                 }
            }
            endOfApplicationProcessing();
        }  // End of main() method

/**********************************************************************************************************
 * main() helper methods
 *********************************************************************************************************/

public String displayMenuAndGetResponse() {

        int response = -1;  // initialze response to invalid value to be sure we store what user enters

        System.out.println("\nWattaYaWannaDo? (enter number of option)\n");
        for (int i = 0; i < mainMenuOptions.length; i++) {
                System.out.println(i + 1 + ". " + mainMenuOptions[i]);
        }
        System.out.print("\nYour choice: ");
        try {
                response = Integer.parseInt(userKeyboardDevice.nextLine());
                return mainMenuOptions[response - 1];
        }
        catch (NumberFormatException exceptionObject) {
                throw new InvalidMenuResponseException("Invalid menu option " + response + " entered: ");
        }
        catch (ArrayIndexOutOfBoundsException exceptionObject) {
                throw new InvalidMenuResponseException("Invalid menu option " + response + " entered");
        }
}  // End of displayMenuAndGetResponse()

        public void startOfApplicationProcessing() {
                System.out.println("-".repeat(60) + "\nWelcome to our app!\nYou rang?\n" + "-".repeat(60));
        }
        public void endOfApplicationProcessing() {
                System.out.println("-".repeat(60) + "\nThank you for using our app!\n" + "-".repeat(60));
        }

        public void displayAllPeople() {
        int personCount = 0;
        String borderIcon = Emogis.TELEVISION;

        System.out.println("\n"+ (borderIcon + " ").repeat(13)) ;
        for (Person anAddams : theAddamsFamily) {
                personCount++;
                System.out.printf("%s %2d. %-30s %-8s",borderIcon,personCount,anAddams.getName(),borderIcon);
                if (personCount != theAddamsFamily.size()) {
                        System.out.println("");
                }
        }
        System.out.println("\n"+ (borderIcon + " ").repeat(13)) ;
        }
        public void displayByName() {
                List<Person> listOfAddams = new LinkedList<>();

                AddamsSearchCriteria whatTheyWant = solicitAddamsSearchCriteria();

                listOfAddams = findAnAddamsByName(whatTheyWant.getSearchValue().strip(),whatTheyWant.isCaseSensitiveSearch());

                System.out.println("\nNumber of Addams' found containing " + whatTheyWant.getSearchValue() + " in name: " + listOfAddams.size());

                for(Person anAddams : listOfAddams) {
                        System.out.printf("%10d %-30s\n",anAddams.getId(),anAddams.getName());
                }
        }
        public void changePersonName() {
                List<Person> listOfAddams = new LinkedList<>();
                AddamsSearchCriteria whatTheyWant = solicitAddamsSearchCriteria();
                listOfAddams = findAnAddamsByName(whatTheyWant.getSearchValue().strip(),whatTheyWant.isCaseSensitiveSearch());

                for(Person anAddams : listOfAddams) {
                        System.out.println("Found: " + anAddams);
                        System.out.println("Do you want to change their name? (Y or N default is No");
                        String changeResponse = userKeyboardDevice.nextLine().strip().toUpperCase();
                        if (changeResponse.startsWith("Y")) {
                                System.out.println("Please enter new name: ");
                                String newName = userKeyboardDevice.nextLine().strip();
                                findAnAddamsById(anAddams.getId()).setName(newName);
                                System.out.println("----- Name changed to: " + newName);
                        }
                        else {
                                System.out.println("----- Name is unchanged -----");
                        }
                }

        }
        public void removeAnAddams() {
                List<Person> aListOfAddams = new ArrayList<>();
                AddamsSearchCriteria whatTheyWant = solicitAddamsSearchCriteria();
                aListOfAddams = findAnAddamsByName(whatTheyWant.getSearchValue().strip(), whatTheyWant.isCaseSensitiveSearch());

                for (Person anAddams : aListOfAddams) {
                        System.out.println("Found: " + anAddams);
                        System.out.println("Do delete this Addams from the database? (Y or N default is No");
                        String deleteResponse = userKeyboardDevice.nextLine().strip().toUpperCase();
                        if (deleteResponse.startsWith("Y")) {
                                if (theAddamsFamily.remove(anAddams)) {
                                        aListOfAddams.remove(anAddams);
                                    System.out.println("----- Removal of " + anAddams.getName() + " was successful");
                                } else {
                                    System.out.println("----- Removal of " + anAddams.getName() + " failed");
                                }
                        }
                }
        }
        public AddamsSearchCriteria solicitAddamsSearchCriteria() {
                String response = "";
                String personToFind = "";
                boolean wantsCaseSensitiveSearch;

                System.out.println("\nPlease enter part or all of the person would like to find");
                response = userKeyboardDevice.nextLine();
                personToFind = response.strip();

                System.out.println("\nWould you like the search to be case sensitive? (Y or N) - default is No");
                response = userKeyboardDevice.nextLine();

                if (response.toUpperCase().startsWith("Y")) {
                        wantsCaseSensitiveSearch = true;
                }
                else {
                        wantsCaseSensitiveSearch = false;
                }
                return new AddamsSearchCriteria(personToFind, wantsCaseSensitiveSearch);
        }

        private void loadFamilyMembersFromFile() throws FileNotFoundException, DataFileErrorException {

                PrintStream fileProcessingErrorLogFile = new PrintStream("fileProcessingError.log");
                System.setErr(fileProcessingErrorLogFile);

                String aLine = null;   // Hold a line from the file
                String MEMBERS_FILE_NAME = "theAddamsFamilyMembers.txt";
                File theAddamsFamilyFile = null;
                Scanner memberFileReader  = null;

                try {
                        theAddamsFamilyFile = new File(MEMBERS_FILE_NAME);
                        memberFileReader  = new Scanner(theAddamsFamilyFile);
                        while (memberFileReader.hasNextLine()) {
                                aLine = memberFileReader.nextLine().strip();
                                theAddamsFamily.add(new Person(aLine));
                        }
                }
                catch(FileNotFoundException exceptionObj) {
                        System.err.println(exceptionObj.getMessage());
                        exceptionObj.printStackTrace();
                        throw new DataFileErrorException(MEMBERS_FILE_NAME + " not found - see error log for details");
                }
                catch (IllegalStateException exceptionObject) {
                        System.err.println("Error processing family member file: " + MEMBERS_FILE_NAME);
                        System.err.println("Call stack:");
                        exceptionObject.printStackTrace();

                        System.out.println("Error processing family member file: " + MEMBERS_FILE_NAME);
                        System.out.println("Please see " + fileProcessingErrorLogFile.toString() + " for details");
                        System.err.println("System message: " + exceptionObject.getMessage() );
                }
        }  // End of loadMembers()

        public Person findAnAddamsById(int personId) {
                Person foundPerson = null;

                for(Person currentAddams : theAddamsFamily) {
                        if (currentAddams.getId() == personId) {
                                foundPerson = currentAddams;
                                break;
                        }
                }

                return foundPerson;
        }
        public List<Person>findAnAddamsByName(String personName, boolean isCaseSensitive) {
                List<Person> foundPeople = new LinkedList<>();

                boolean personFound;

                List<Person> theAddamsFamilyList =new ArrayList<>(theAddamsFamily);

                for(Person currentAddams : theAddamsFamily) {
                        personFound = false;
                        switch(Boolean.toString((isCaseSensitive))) {
                                case "false":
                                        if (currentAddams.getName().toUpperCase().contains(personName.toUpperCase())) {
                                                personFound = true;
                                        }
                                        break;
                                case "true":
                                        if (currentAddams.getName().contains(personName)) {
                                                personFound = true;
                                        }
                                        break;

                        }
                        if (personFound) {
                                foundPeople.add(currentAddams);
                        }
                }
                return foundPeople;
        }


} // End of ApplicationProgram class
