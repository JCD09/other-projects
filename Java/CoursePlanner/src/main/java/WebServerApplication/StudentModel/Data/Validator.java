package WebServerApplication.StudentModel.Data;

import WebServerApplication.StudentModel.Components.Record;
import org.apache.poi.hslf.exceptions.InvalidRecordFormatException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Class for validating input strings;
public class Validator {
    private static final Pattern instrWithoutQMarks = Pattern.compile("\\s*\\\"([^\\\"]*)\\\"\\s*");

    private String line;

    private Validator(){

    }
    public static Record getRecord(String line){
        Validator v = new Validator();
        Matcher matcher=instrWithoutQMarks.matcher(line);
        List<String > instructorsList;
        String newSubstring=line;
        if(matcher.find()){
            String instructorsStr = line.substring(matcher.start(),matcher.end());
            newSubstring = line.replace(instructorsStr," ");
            instructorsList= v.addInstructors(v.removeQuotationMarks(instructorsStr));
        }
        else{
            String[] s = newSubstring.split(",");
            assert(s.length==8);
            instructorsList=new ArrayList<>();
            instructorsList.add(s[6].trim());
        }
        String[] s = newSubstring.split(",");
        assert(s.length==8);
        int semester = Integer.parseInt(s[0].trim());
        String subject = s[1].trim();
        String catalogueNumber = s[2].trim();
        String location = s[3].trim();
        int enrollmentCapacity = Integer.parseInt(s[4].trim());
        int enrollmentTotal = Integer.parseInt(s[5].trim());
        String componentCode = s[7].trim();


        Record newRecord = new Record.NestedRecordBuilder()
                .addSemesterCode(semester)
                .addSubject(subject)
                .addCatalogueNumber(catalogueNumber)
                .addLocation(location)
                .addEnrollment(enrollmentCapacity,enrollmentTotal)
                .addInstructors(instructorsList)
                .addComponentCode(componentCode).createNewRecord();
        return newRecord;
    }
    public static List<String> addInstructors(String instructors){
        String[] listinstructors = instructors.split(",");
        for(String instructor: listinstructors){instructor.trim();}

        return Arrays.asList(listinstructors);
    }

    public static String removeQuotationMarks(String substring){
        String replaced = substring.replaceAll("\""," ");
        //System.out.println(replaced+"\n");
        //String newreplaced = replaced.replaceFirst("\\,"," ");
        return replaced;
    }

    private Pattern semesterPattern = Pattern.compile("(\\s*\\d{4}\\s*,)");
    private Pattern subjectPattern = Pattern.compile("(\\s*\\w{3,4}\\s*,)");
    private Pattern catalogueNumberPattern = Pattern.compile("(\\s*\\w{3,4}\\s*,)(\\s*\\d{3}[a-zA-Z]?\\s*,)");
    private Pattern locationPattern = Pattern.compile("(\\s*[A-Z]{3,}\\s*,)");
    private Pattern componentCodePattern = Pattern.compile("(\\s*[A-Z]{3}\\s*)");
    private Pattern enrollmentAndcapacityPattern = Pattern.compile("((\\s*[0-9]{1,}\\s*,){2})");

    private String extractString(String line,Pattern pattern) throws InvalidRecordFormatException{
        Matcher matcher=pattern.matcher(line);
        if(matcher.find()){
            String substring = line.substring(matcher.start(),matcher.end()-1);
            line = line.replace(substring," ");
            //System.out.println(line+"\n");
            return substring;
        }
        else{
            throw new InvalidRecordFormatException("Could not extract/validate semester from the following string:"+line+"\n");
        }
    }




}
