import React from "react";
import {Container} from "react-bootstrap";
import {Student} from "../types/Student.type";


const StudentDetails = (props:{student:Student}) => {


    return (
        <Container fluid>
            <h4>Student Details</h4>
            <h6>name: {props.student.name}</h6>
            <h6>email: {props.student.email}</h6>
            <h6>birthday: {props.student.birthday}</h6>
            <h6>address: {props.student.address}</h6>
        </Container>
    )
}

export default StudentDetails;