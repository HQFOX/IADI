import {Form, Spinner, Table} from "react-bootstrap";
import React, {ChangeEvent, useEffect, useState} from "react";
import '../css/routerpage.css';
import {FaFilter} from 'react-icons/fa';
import {useHistory} from 'react-router-dom';
import DatePicker from 'react-date-picker';
import {formatDateQueryDot, FormatDateSlash} from "../utils/DateUtil";
import '../css/table.css';

function GrantTable() {

    useEffect(() => {
        fetchGrants(formatDateQueryDot(new Date()), 0, 100000);
    }, []);

    const history = useHistory();
    const [items, setItems] = useState([]);
    const [filteredItems, setFilter] = useState([]);
    const [isLoaded, setIsLoaded] = useState(false);
    const [dateValue, setDateValue] = useState(new Date());
    const [expandFilter, setExpandFilter] = useState(false);

    const [minimumValue, setMinimumValue] = useState(0);
    const [maximumValue, setMaximumValue] = useState(100000);

    const handleExpandFilter = () => setExpandFilter(!expandFilter);

    const handleCalendarChange = (e:any) => {
        setDateValue(e);
        fetchGrants(formatDateQueryDot(e), minimumValue, maximumValue)
    }

    const handleMinimum = (e:any) => {
        setMinimumValue(e.target.value);
        fetchGrants(formatDateQueryDot(dateValue), e.target.value, maximumValue)
    }

    const handleMaximum = (e:any) => {
        setMaximumValue(e.target.value);
        fetchGrants(formatDateQueryDot(dateValue), minimumValue, e.target.value);
    }

    const fetchGrants = async (date: String, minimum: number, maximum: number) => {
        let path = '/grantcall/filter?date=' + date + "&minimum=" + minimum + "&maximum=" + maximum;
        await fetch(path)
            .then(res => res.json())
            .then(json => {
                json.sort((a:any,b:any) => (a.closeDate > b.closeDate) ? 1 : -1)
                setItems(json);
                setFilter(json);
                setIsLoaded(true);
            });
    }

    const handleFilter = (e: ChangeEvent<HTMLInputElement>) => {
        let searchFilter = e.target.value;
        if (searchFilter === '')
            setFilter(items);
        else
            setFilter(items.filter((p: any) => p.title.toLowerCase().includes(searchFilter.toLowerCase())));
    };

    const handleRowClick = (key: any) => {
        history.push("/grant/"+key);
    }


    if (!isLoaded)
        return (
            <div className="loading"><Spinner animation="border"/></div>
        )
    else
        return (
            <div className="innerBox">
                { expandFilter ? (<div className="filterArea">
                    <div className="title">Filter Settings</div>
                    <hr/>
                    <div className="field">
                        Minimum funding:
                        <Form.Control className="fieldinput" onChange={handleMinimum} value={minimumValue} />
                    </div>
                    <div className="field">
                        Maximum funding:
                        <Form.Control className="fieldinput" onChange={handleMaximum} value={maximumValue} />
                    </div>
                    <hr/>
                    <div className="field">
                        On Date <DatePicker onChange={handleCalendarChange} value={dateValue}/>
                    </div>
                </div>) : null}
                <div className="searchArea">
                    <Form.Control className="searchText" placeholder="Search here..."
                                  onChange={handleFilter} />
                    <button className="filterButton" onClick={handleExpandFilter}><FaFilter/></button>
                </div>
                <Table striped bordered hover>
                    <thead>
                    <tr>
                        <th style={{width: "50%"}}>Title</th>
                        <th style={{width: "15%"}}>Funding</th>
                        <th style={{width: "15%"}}>Open Date</th>
                        <th style={{width: "15%"}}>Close Date</th>
                        <th style={{width: "5%"}}>Number of Applications</th>
                    </tr>
                    </thead>
                    <tbody>
                    {filteredItems.map((item: any) => (
                        <tr key={item.grantCallId} onClick={() => handleRowClick(item.grantCallId)}>
                            <td>{item.title}</td>
                            <td>{item.funding}</td>
                            <td>{FormatDateSlash(item.openDate)}</td>
                            <td>{FormatDateSlash(item.closeDate)}</td>
                            <td>{item.numberApps}</td>
                        </tr>
                    ))}
                    </tbody>
                </Table>
            </div>
        )
}

export default GrantTable;
