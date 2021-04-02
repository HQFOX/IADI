import React, {ChangeEvent, useEffect, useState} from 'react';
import {Form, Table} from "react-bootstrap";
import {FaFilter} from "react-icons/fa";
import {useHistory} from 'react-router-dom';
import {formatDateQueryDot, FormatDateSlash} from "../utils/DateUtil";

export interface Props {
    tableTitle: string;
    data: any[];
    subForm?: (id: number) => void
}

function ApplicationTable({tableTitle, data, subForm}: Props) {
    const history = useHistory();
    const [filteredItems, setFilterItems] = useState(data);
    const [filter, setFilter] = useState("");

    const handleFilter = (e: ChangeEvent<HTMLInputElement>) => {
        let searchFilter = e.target.value;
        setFilter(searchFilter);
        if (searchFilter === '')
             return setFilterItems(data);
        else
            return setFilterItems(data.filter((p: any) => p.grantTitle.toLowerCase().includes(searchFilter.toLowerCase())));
    };

    const handleRowClick = (key: any) => {
        if (subForm != null)
            subForm(key);
    }

    const ListOfItems = () => filter === '' ? data : filteredItems;
    
    return (
        <div className="innerBox">
            {tableTitle}
            <div className="searchArea">
                <Form.Control className="searchText" placeholder="Search ..."
                              onChange={handleFilter}></Form.Control>
                <button className="filterButton"><FaFilter/></button>
            </div>
            <Table striped bordered hover>
                <thead>
                <tr>
                    <th style={{width: "15%"}}>My App ID</th>
                    <th style={{width: "35%"}}>Title</th>
                    <th style={{width: "50%"}}>Funding</th>
                    <th style={{width: "15%"}}>Close Date</th>
                    <th style={{width: "15%"}}>Status</th>
                </tr>
                </thead>
                <tbody>
                {ListOfItems().map((item: any) => (
                    <tr key={item.applicationId} onClick={() => handleRowClick(item.applicationId)}>
                        <td>{item.applicationId}</td>
                        <td>{item.grantTitle}</td>
                        <td>{item.grantFunding}</td>
                        <td>{FormatDateSlash(item.grantCloseDate)}</td>
                        <td>{item.status}</td>
                    </tr>
                ))}
                </tbody>
            </Table>
        </div>
    )
}

export default ApplicationTable;