package com.martin.studentsApi.web.rest;

import com.martin.studentsApi.model.StudyProgram;
import com.martin.studentsApi.model.exceptions.StudyProgramNotExistException;
import com.martin.studentsApi.service.StudyProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/api/study-programs", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class StudyProgramsRestController {

    private StudyProgramService service;

    @Autowired
    public StudyProgramsRestController(StudyProgramService service) {
        this.service = service;
    }

    @RequestMapping(method = GET)
    public Iterable<StudyProgram> getAllStudyPrograms() {
        return service.findAllStudyPrograms();
    }

    @RequestMapping(value="{id}", method = GET)
    public StudyProgram getAllStudyPrograms(@PathVariable String id) {
        return service.findStudyProgramById(Long.parseLong(id));
    }

    @RequestMapping(value = "{id}", method = DELETE)
    public ResponseEntity<?> deleteStudyProgram(@PathVariable String id) {
        try {
            service.deleteStudyProgramById(Long.parseLong(id));
            return ResponseEntity.status(HttpStatus.OK).body(0);
        } catch (StudyProgramNotExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cannot delete study program");
        }
    }

    @RequestMapping(method = POST)
    public ResponseEntity<?> addStudyProgram(@RequestBody StudyProgram studyProgram) {
        try {
            StudyProgram sp = service.saveStudyProgram(studyProgram);
            URI locationUri = URI.create("http://localhost:8080/studentsApi/study_programs/" + sp.getId());
            return ResponseEntity.created(locationUri).body(sp);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Study Program with the name '" + studyProgram.getName() + "' already exists");
        }
    }

    @RequestMapping(value = "{id}", method = PATCH)
    public ResponseEntity<StudyProgram> editStudyProgram(@PathVariable String id,
                                           @RequestBody StudyProgram studyProgram) {
            StudyProgram sp = service.updateStudyProgram(Long.parseLong(id), studyProgram);
            return ResponseEntity.ok(sp);
    }

}
