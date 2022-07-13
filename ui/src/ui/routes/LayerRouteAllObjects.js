
import React, { useEffect, useState } from "react";
import { Marker, Polyline, Popup, Tooltip, useMapEvents } from "react-leaflet";
import MapLabel from "../markers/MapLabel";
import axios from 'axios';
import { Button, IconButton } from "@mui/material";
import {LocalTaxi, PersonAddAlt1Outlined, Person} from '@mui/icons-material';


const LayerRouteAllObjects = (props) => {
    const [allRoutes, setAllRoutes] = useState([]);

    useEffect(()=> {
        let url = 'http://localhost:8080/api/ui/routes/all'
        axios.get(url).then( (response) => {
            console.log(response.data)
            setAllRoutes(response.data);
        })
        .catch((error) => {
          console.log(error);
        });
    }, [props.forceRerender])

    let allPolylines = [];
    allRoutes.forEach((element, index) => {
        const path1 = (<Polyline key={`paht1${index}`} color="green" positions={element.pathTaxiToPassanger} />);
        const path2 = (<Polyline key={`paht2${index}`} color="red" positions={element.pathTaxiToDestination} />);
        element.pathTaxiToPassanger && allPolylines.push(path1);
        element.pathTaxiToDestination && allPolylines.push(path2);
    });
    

    return <>{allPolylines}</>
}

export default LayerRouteAllObjects;
