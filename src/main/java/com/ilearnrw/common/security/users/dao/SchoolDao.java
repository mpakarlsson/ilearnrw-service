package com.ilearnrw.common.security.users.dao;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import java.util.List;

import com.ilearnrw.common.security.users.model.Classroom;
import com.ilearnrw.common.security.users.model.School;
import com.ilearnrw.common.security.users.model.User;

public interface SchoolDao {

	public int addSchool(School school);

	public School getSchool(String name);

	public int addClassroomToSchool(School school, Classroom classroom);

	public Classroom getClassroom(String name);

	public void assignStudentToClassroom(User student, Classroom classroom);

	public List<School> getListOfSchools();

	public List<Classroom> getClassroomsBySchool(School school);

	public Classroom getStudentClassroom(User student);

	public School getClassroomSchool(Classroom classroom);

	public List<User> getStudentsFromClassRoom(Classroom classroom);

}