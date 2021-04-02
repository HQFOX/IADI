import {Button, Form, Table} from "react-bootstrap";
import {FaFilter} from "react-icons/fa";
import React, {ChangeEvent, useState} from "react";
import NewAppModal from "./NewAppModal";
import {useDispatch, useSelector} from "react-redux";
import {RootStore} from "../Store";

export interface Props {
    apps: any;
    grantTitle: string;
    grantDesc: string;
    dataitems: any[];
    grantId: number;
    refreshTable: () => void;
}

function ApplicationsTable({apps, grantTitle, grantDesc, dataitems, grantId, refreshTable}: Props) {

    const dispatch = useDispatch();
    const login = useSelector((state: RootStore) => state.login);

    const [show, setShow] = useState(false);
    let showModal = () => setShow(true);
    let hideModal = () => {
        setShow(false);
        //refreshTable();
    }


    const handleFilter = (e: ChangeEvent<HTMLInputElement>) => {
        console.log(e.target.value);
    }

    const handleRowClick = (key: any) => {
        //history.push("/"+key);
        console.log(key);
    }

    return (
        <>
            <NewAppModal show={show} handleClose={hideModal} grantTitle={grantTitle} grantDesc={grantDesc} dataitems={dataitems} grantId={grantId}/>
            <div className="detail-applications">
                <div className="searchArea">
                    <Form.Control className="searchText" placeholder="Search here..."
                                  onChange={handleFilter}></Form.Control>
                    { login.currentRole === "ROLE_STUDENT" ? (<Button onClick={showModal} variant="outline-success">Apply to Grant</Button>) : (null) }
                    <button className="filterButton"><FaFilter/></button>
                </div>
                <Table striped bordered hover>
                    <thead>
                    <tr>
                        <th style={{width: "5%"}}>Application Number</th>
                        <th style={{width: "30%"}}>Introduction</th>
                        <th style={{width: "30%"}}>Related Work</th>
                        <th style={{width: "25%"}}>Publications</th>
                        <th style={{width: "10%"}}>Status</th>
                    </tr>
                    </thead>
                    <tbody>
                    {apps.map((item: any) => (
                        <tr key={item.applicationId} onClick={() => handleRowClick(item.applicationId)}>
                            <td>{item.applicationId}</td>
                            <td>{item.introduction}</td>
                            <td>{item.relatedWork}</td>
                            <td>{item.publications}</td>
                            <td>{item.status}</td>
                        </tr>
                    ))}
                    </tbody>
                </Table>
            </div>
        </>
    )
}

export default ApplicationsTable