import React, {Component} from 'react';

import {Spinner} from 'react-bootstrap'
import StudentsTable from "../components/StudentsTable";

class StudentPage extends Component<any, any> {

    constructor(props:{}) {
        super(props);
        this.state = {
            items: [],
            isLoaded: false,
            showModal: false,
        }
    }


    componentDidMount() {
        fetch('/student')
            .then(res => res.json())
            .then(json => {
                this.setState({
                    isLoaded: true,
                    items: json,
                })
                console.log(json);
            });
    }

    showModal = () => {
        this.setState({ show: true });
    };

    hideModal = () => {
        this.setState({ show: false });
    };

    render() {
        var {isLoaded, items, show} = this.state;
        if (!isLoaded) {
            return (
                <div className={"App"}>
                    <Spinner animation="border" />
                </div>
            )
        }
        else {
            return (
                <div className={"App"}>
                <StudentsTable data={items}/>
                </div>
            )
        }
    }
};

export default StudentPage;