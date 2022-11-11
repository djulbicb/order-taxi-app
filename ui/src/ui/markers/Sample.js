import React, { useState } from 'react';
import { faHome, faUserAstronaut, faRankingStar } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Marker, Popup, Tooltip, Polyline } from 'react-leaflet';
import ReactDOMServer from 'react-dom/server';
import * as L from 'leaflet';
import axios from 'axios';
import MarkerPopup from './MarkerPopup';

function Sample(props) {
    
    // [52.5200, 13.4050],
    //   [52.51, 13.5050]
    const [polylineToPassanger, setPolylineToPassanger] = useState([]);
    const [polylineToDestination, setPolylineToDestination] = useState([]);
      
    

    const iconHTML = ReactDOMServer.renderToString(<FontAwesomeIcon icon={faUserAstronaut}/>)
    const customMarkerIcon = L.divIcon({
      html: iconHTML,
      className: 'marker-astronaut'
    });


    return (
      <>
        <Marker icon={customMarkerIcon} position={props.position}></Marker>
      </>
        
      );
  }
  
  export default Sample;