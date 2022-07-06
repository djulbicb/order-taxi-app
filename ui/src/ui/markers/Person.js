import React, { useState } from 'react';
import { faHome, faUserAstronaut, faTaxi, faUserLarge } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Marker, Popup } from 'react-leaflet';
import ReactDOMServer from 'react-dom/server';
import * as L from 'leaflet';
import axios from 'axios';
import MarkerPopup from './MarkerPopup';

function Person(props) {

    const iconHTML = ReactDOMServer.renderToString(<FontAwesomeIcon icon={faUserLarge}/>)
    const customMarkerIcon = L.divIcon({
      html: iconHTML,
      className: 'marker-person'
    });

    const [notifications, setNotifications] = useState([]);
    

    const eventHandlers={
      popupopen: (e) => {
        console.log(e);

        const url = `http://localhost:8080/api/notifications/${props.id}`;

        axios.get(url)
        .then(function (response) {
          console.log(response);
          setNotifications(response.data);
        })
        .catch(function (error) {
          console.log(error);
        });



        const urlll = `http://localhost:8080/api/contract/${props.type}/${props.id}`;
        axios.get(urlll)
        .then(function (response) {
          console.log(response);

        })
        .catch(function (error) {
          console.log(error);
        });

      },
      popupclose: (e) => {
        console.log("close")
      }
    };

    return (
        <Marker  icon={customMarkerIcon} position={props.position} eventHandlers={eventHandlers}>  
             <MarkerPopup id={props.id} position={props.position} notifications={notifications}></MarkerPopup>
        </Marker>
      );
  }
  
  export default Person;