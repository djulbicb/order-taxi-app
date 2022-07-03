
import React, { useEffect, useState } from "react";
import { Marker, Polyline, Popup, Tooltip, useMapEvents } from "react-leaflet";
import MapLabel from "../markers/MapLabel";
import axios from 'axios';
import { Button, IconButton } from "@mui/material";
import {LocalTaxi, PersonAddAlt1Outlined, Person} from '@mui/icons-material';


const PlaceholderLayer = (props) => {
    const [coordinates, setCoordinates] = useState([]);

    useEffect(()=> {
        let url = "";
        if (props.placeholderType === 'nearest') {
            url = 'http://localhost:8080/api/placeholders/nearest';
        } else {
            url = 'http://localhost:8080/api/placeholders/matrix';
        }

        console.log(props.placeholderType)

        axios.get(url)
        .then(function (response) {
          console.log(response);
          setCoordinates(response.data);
        })
        .catch(function (error) {
          console.log(error);
        });
  }, [props.placeholderType])

  const handleAddTaxi = (position) => {
    console.log(position);

    const coordinateFromPosition = {
        lat: position[0],
        lng: position[1]
    }

    axios.post('http://localhost:8080/api/placeholders/taxi', coordinateFromPosition)
    .then(function (response) {
        console.log(response);
    }).catch(function (error) {
        console.log(error);
    });
  }

  const handleAddPerson = (position) => {
    console.log(position);

    const coordinateFromPosition = {
        lat: position[0],
        lng: position[1]
    }

    axios.post('http://localhost:8080/api/placeholders/passanger', coordinateFromPosition)
    .then(function (response) {
        console.log(response);
    }).catch(function (error) {
        console.log(error);
    });
 }

  const markers = coordinates.map(mark=> {
    return (
        <Marker key={mark} position={mark} eventHandlers={{
          popupopen: (e) => {
            console.log(e)
          },
          popupclose: (e) => {
            console.log("close")
          }
        }}>
            <Popup>
              <h3>{mark[0]}, {mark[1]}</h3>
              Create
              <IconButton aria-label="fingerprint" color="secondary" onClick={()=>handleAddTaxi(mark)}>
                <LocalTaxi/>
              </IconButton>
              or
              <IconButton aria-label="fingerprint" color="success"  onClick={()=>handleAddPerson(mark)}>
                <Person/>
              </IconButton>
            </Popup>
      </Marker>
    )
  });
    return <>{markers}</>
}

export default PlaceholderLayer