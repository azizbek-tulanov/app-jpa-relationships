package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    SubjectRepository subjectRepository;

    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }

    //3. FACULTY DEKANAT
    @GetMapping("/forfacultydekanat/{facultyId}")
    public Page<Student> getStudentListForFaculty(@PathVariable Integer facultyId, @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_FacultyId(facultyId, pageable);
        return studentPage;
    }

    //4. GROUP OWNER
    @GetMapping("/forgroupowner{groupId}")
    public Page<Student> getStudentListForGroup(@PathVariable Integer groupId, @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroupId(groupId, pageable);
        return studentPage;
    }

    @PostMapping("/addStudent")
    public HttpEntity<?> saveOrEditStudent(@RequestBody StudentDto studentDto) {
        Student student = new Student();
        if (studentDto.getId() != null) {
            Optional<Student> studentOptional = studentRepository.findById(studentDto.getId());
            if (studentOptional.isPresent()) {
                student = studentOptional.get();
            }
        }
        student.setFirstName(studentDto.getFirstName());

        student.setLastName(studentDto.getLastName());

        Optional<Group> groupOptional = groupRepository.findById(studentDto.getGroupId());
        if (!groupOptional.isPresent()) {
            return ResponseEntity.status(405).body("Group Not Found");
        }
        student.setGroup(groupOptional.get());

        List<Subject> subjectList = new ArrayList<>();
        for (Integer subjectId : studentDto.getSubjectIds()) {
            Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);
            if (!optionalSubject.isPresent()) {
                return ResponseEntity.status(405).body("Subject Not Found");
            }
            subjectList.add(optionalSubject.get());
        }
        student.setSubjects(subjectList);

        Address address = new Address();
        address.setCity(studentDto.getAddressDto().getCity());
        address.setStreet(studentDto.getAddressDto().getStreet());
        address.setDistrict(studentDto.getAddressDto().getDistrict());
        Address savedAddress = addressRepository.save(address);
        student.setAddress(savedAddress);

        studentRepository.save(student);
        return ResponseEntity.ok("Saved Student");
    }

    @PutMapping("/editStudent")
    public HttpEntity<?> editStudent(@RequestBody StudentDto studentDto) {
        Optional<Student> studentOptional = studentRepository.findById(studentDto.getId());
        if (!studentOptional.isPresent()) {
            return ResponseEntity.status(405).body("Student Not found");
        }
        Student student = studentOptional.get();
        student.setFirstName(studentDto.getFirstName());

        student.setLastName(studentDto.getLastName());

        Optional<Group> groupOptional = groupRepository.findById(studentDto.getGroupId());
        if (!groupOptional.isPresent()) {
            return ResponseEntity.status(405).body("Group Not Found");
        }
        student.setGroup(groupOptional.get());

        List<Subject> subjectList = new ArrayList<>();
        for (Integer subjectId : studentDto.getSubjectIds()) {
            Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);
            if (!optionalSubject.isPresent()) {
                return ResponseEntity.status(405).body("Subject Not Found");
            }
            subjectList.add(optionalSubject.get());
        }
        student.setSubjects(subjectList);

        Address address = new Address();
        address.setCity(studentDto.getAddressDto().getCity());
        address.setStreet(studentDto.getAddressDto().getStreet());
        address.setDistrict(studentDto.getAddressDto().getDistrict());
        Address savedAddress = addressRepository.save(address);
        student.setAddress(savedAddress);

        studentRepository.save(student);
        return ResponseEntity.ok("Student Edited");
    }

    @DeleteMapping("/delete{id}")
    public HttpEntity<?> deleteStudent(@PathVariable Integer id) {
        try {
            studentRepository.deleteById(id);
            return ResponseEntity.ok("Student Deleted");
        }
        catch (Exception e) {
            return ResponseEntity.status(405).body("Student Not Found");
        }
    }
}
