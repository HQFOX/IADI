import React from "react";
import {Accordion, Button, Card, Container} from "react-bootstrap";
import {DataItem} from "../types/DataItem.type";
import {AppDataItem} from "../types/AppDataItem.type";

const DataItemsTable = (props:{dataitems:DataItem[],appdataitems:AppDataItem[]}) => {

    const matchItems = (dataItemId:number) => {
        const appDataItem: AppDataItem[] = props.appdataitems.filter( (appdataitem:AppDataItem) => dataItemId === appdataitem.dataItemId)
        console.log(appDataItem)
        if (appDataItem.length > 0)
            return appDataItem[0].data
        return "Not submitted"
    }

    return (
        <Container fluid>
            <h4>Data Items</h4>
            <Accordion>
                { props.dataitems.map( dataitem =>
                    <Card key={dataitem.dataItemId}>
                        <Card.Header>
                            <Accordion.Toggle as={Button} variant="link" eventKey={dataitem.dataItemId.toString()}>
                                {dataitem.dataType}
                            </Accordion.Toggle>
                        </Card.Header>
                        <Accordion.Collapse eventKey={dataitem.dataItemId.toString()}>
                            <Card.Body>{matchItems(dataitem.dataItemId)}</Card.Body>
                        </Accordion.Collapse>
                    </Card>
                )}
            </Accordion>

        </Container>
    )
}

export default DataItemsTable;