import {Table} from "react-bootstrap";
import React from "react";

export interface Props {
    data: [];
}
/*var name: String,
        var birthday: String,
        var email: String,
        var address: String,
        var telephone: String,
        var city: String,
        var postcode: String*/

function StudentsTable({data}: Props) {
    return (
        <Table striped bordered hover>
            <thead>
            <tr>
                <th>Name</th>
                <th>Birthday</th>
                <th>Email</th>
                <th>Address</th>
                <th>Telephone</th>
                <th>City</th>
                <th>Postcode</th>
            </tr>
            </thead>
            <tbody>
            {data.map((item:any) => (
                <tr>
                    <td>{item.name}</td>
                    <td>{item.birthday}</td>
                    <td>{item.email}</td>
                    <td>{item.address}</td>
                    <td>{item.telephone}</td>
                    <td>{item.city}</td>
                    <td>{item.postcode}</td>
                </tr>
            ))}
            </tbody>
        </Table>
    );
}

export default StudentsTable;
