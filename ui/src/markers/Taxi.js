import React, { useState } from 'react';
import { faHome, faUserAstronaut, faTaxi } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Marker, Popup } from 'react-leaflet';
import ReactDOMServer from 'react-dom/server';
import * as L from 'leaflet';

function Taxi(props) {

    const iconHTML = ReactDOMServer.renderToString(<FontAwesomeIcon icon={faTaxi}/>)
    const customMarkerIcon = L.divIcon({
      html: iconHTML,
      className: 'marker-taxi'
    });

    return (
        <Marker  icon={customMarkerIcon} position={props.position} eventHandlers={props.eventHandlers}>  
            <Popup>
            A pretty CSS3 popup. <br /> Easily customizable.
            </Popup>
        </Marker>
      );
  }
  
  export default Taxi;