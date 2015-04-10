//package com.umpay;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//  
//
//import java.util.Map;
//
//import com.thoughtworks.xstream.XStream;
//import com.thoughtworks.xstream.converters.Converter;
//import com.thoughtworks.xstream.converters.MarshallingContext;
//import com.thoughtworks.xstream.converters.UnmarshallingContext;
//import com.thoughtworks.xstream.io.HierarchicalStreamReader;
//import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
//import com.thoughtworks.xstream.io.xml.DomDriver;
//  
//public class Test {
//  
//    public static void main(String[] args) {
//        test4();
//    }
//  
//    private static void test4() {
//        XStream xs = new XStream(new DomDriver());
//        List<Student> stulist = new ArrayList<Student>();
//        Student stu = new Student();
//        stu.setName("wxg");
//        stu.setCourse("English");
//        stulist.add(stu);
//        stulist.add(stu);
//        
//        Map<String,Student> stuMap = new HashMap<String, Student>();
//        stuMap.put("stu1", stu);
//        stuMap.put("stu2", stu);
//  
//        xs.alias("student", Student.class);
//  
//        xs.setMode(XStream.NO_REFERENCES);
////        xs.registerConverter(new StudentListConverter());
//        String str = xs.toXML(stuMap);
//        System.out.println(str);
//        stuMap = (Map<String,Student>)xs.fromXML(str);
//        System.out.println(stulist);
//    }
//  
//    private static class StudentConverter implements Converter {
//        private void strategy01(Student stu, HierarchicalStreamWriter writer) {
//            //writer.startNode(name);
//            writer.addAttribute("name", stu.getName());
//            writer.addAttribute("course", stu.getCourse());
//        }
//  
//        private void strategy02(Student stu, HierarchicalStreamWriter writer) {
//            writer.startNode("name");
//            writer.setValue(stu.getName());
//            writer.endNode();
//            //
//            writer.startNode("course");
//            writer.setValue(stu.getCourse());
//            writer.endNode();
//            //
//        }
//  
//        public void marshal(Object source, HierarchicalStreamWriter writer,
//                MarshallingContext context) {
//            Student stu = (Student) source;
//            //strategy01(stu, writer);
//            strategy02(stu, writer);
//        }
//  
//        public Object unmarshal(HierarchicalStreamReader reader,
//                UnmarshallingContext context) {
//            return null;
//        }
//  
//        public boolean canConvert(Class type) {
//            return type.equals(Student.class);
//        }
//  
//    }
//  
//    private static class StudentListConverter implements Converter {
//        public void marshal(Object source, HierarchicalStreamWriter writer,
//                MarshallingContext context) {
//            ArrayList al = (ArrayList) source;
//            for (Object obj : al) {
//                context.convertAnother(obj, new StudentConverter());
//            }
//        }
//  
//        public Object unmarshal(HierarchicalStreamReader reader,
//                UnmarshallingContext context) {
//            return null;
//        }
//  
//        public boolean canConvert(Class type) {
//            return type.equals(ArrayList.class);
//        }
//  
//    }
//}
//  
//class Student {
//    private String name;
//    private String course;
//  
//    public String getName() {
//        return name;
//    }
//  
//    public void setName(String name) {
//        this.name = name;
//    }
//  
//    public String getCourse() {
//        return course;
//    }
//  
//    public void setCourse(String course) {
//        this.course = course;
//    }
//}