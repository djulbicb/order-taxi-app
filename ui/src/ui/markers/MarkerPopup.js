import React, { useEffect, useState } from 'react';
import { Popup } from 'react-leaflet';
import axios from 'axios';

function MarkerPopup(props) {

   

    useEffect(()=> {
       
  }, [])
  const lis = props.notifications?.map((elem, index) => {
    return (<li key={"notify" + index}>{elem.message}</li>)
  })

    return (
        <Popup>
            <h3>{props.id}</h3>
            <p>{props.status}</p>
            <hr></hr>
            <ul>
              {lis}
            </ul>

            
        </Popup>
      );
  }
  
  export default MarkerPopup;