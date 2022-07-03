
import React, { useEffect, useState } from "react";
import { MapContainer, Polyline, TileLayer } from "react-leaflet";
import GridLayer from "./ui/grid/GridLayer";
import Person from "./ui/markers/Person";
import Taxi from "./ui/markers/Taxi";
import RoutineMachine from "./RoutineMachine";

import Paper from '@mui/material/Paper';
import { Card, CardContent, Checkbox, FormControl, FormControlLabel, FormLabel, Grid, Radio, RadioGroup, Slider } from "@mui/material";
import { styled } from '@mui/material/styles';
import PlaceholderLayer from "./ui/grid/PlaceholderLayer";
import MinimumDistanceSlider from "./components/minimum_distance_slider/MinimumDistanceSlider";


const fillBlueOptions = { fillColor: 'blue' }

const Mapp = (props) => {
  const [showGrid, setShowGrid] = useState(true);
  const [showPlaceholder, setShowPlaceholder] = useState(true);
  const [placeholderType, setPlaceholderType] = useState('nearest');

  const [viewFilterContract, setViewFilterContract] = React.useState([20, 37]);


  const [center, setCenter] = useState([52.5200, 13.4050])
  const [pos, setPos] = useState([]);
  const [allMarkers, setAllMarkers] = useState([]);
  const [viewportObjectsIds, setViewportObjectsIds]  = useState([]);
  const [viewportObjects, setViewportObjects]  = useState([]);


  /*  VIEWPORT OBJECT IDS*/
  const arraysContainSame = (a, b) => {
    a = Array.isArray(a) ? a : [];
    b = Array.isArray(b) ? b : [];
    return a.length === b.length && a.every(el => b.includes(el));
  }

  useEffect(() => {
    const intervalId = setInterval(() => {

  const request = {
      lat:center[0],
      lng:center[1],
      objects: ["PERSON", "PASSANGER"],
      size: "SIZE_3",
      layer: "ALL"
    }

    const params = getParam(request)

    fetch(`http://localhost:8080/viewport/objects-in-area?${params}`)
          .then(response => response.json())
          .then(data => {
            setViewportObjects(data)
          })
    }, 1000);

    return () => clearInterval(intervalId); //This is important
  },[center])

  //   useEffect(()=> {
  //       axios.post('http://localhost:8080/viewport/objects-by-id', {
  //         ids: viewportObjectsIds
  //       })
  //       .then(function (response) {
  //         console.log(response);
  //         setViewportObjects(response.data);
  //       })
  //       .catch(function (error) {
  //         console.log(error);
  //       });
  // }, [viewportObjectsIds])
  
  // useEffect(()=> {
  //   fetch('http://localhost:8080/waypoints', headers)
  //         .then(response => response.json())
  //         .then(data => {
  //           setPos(data)
  //           console.log(data);
  //         })
  // }, [])

  const [marker, setMarker] = useState([52.5200, 13.4050]);


  const getParam = (o, searchParam = new URLSearchParams) => {
    Object.entries(o).forEach(([k, v]) => {
      if (v !== null && typeof v === 'object')
        getParam(v, searchParam)
      else
        searchParam.append(k, v)
    })
    
    return searchParam
  }



  // [marker[0], marker[1]]
  // [52.6200, 13.5050]
  const markers = allMarkers.map(mark=> {
    const t = mark.split(",");
    return (
        <Taxi key={mark} position={t} eventHandlers={{
          popupopen: (e) => {
            console.log(e)
          },
          popupclose: (e) => {
            console.log("close")
          }
        }}>
      </Taxi>
    )
  });
      

  const handleOnMoveEnd = (coordinate) => {
    setCenter([coordinate.lat, coordinate.lng]);
    
    // updateViewer(coordinate.lng, coordinate.lat)
  }

  const elements = viewportObjects.map((element, index) => {
    const lat = element.coordinate.lat;
    const lng = element.coordinate.lng;
    const coordinate = [lat, lng];

    if (element.status === "TAXI") {
      return <Taxi key={index} id={element.id} position={coordinate}></Taxi>
    } else if (element.status === "PASSANGER") {
      return <Person key={index} id={element.id} position={coordinate}></Person>
    }
  })



  const handleShowGridChange = (event) => {
    setShowGrid(!showGrid);
  };
  const handleShowPlaceholders = (event) => {
    setShowPlaceholder(!showPlaceholder)
  };
  const handelPlaceholderTypeChange = (event) => {
    setPlaceholderType(event.target.value)
  };
  const handelViewFilter = (event) => {
    console.log(event);
  };


  const valuetext = (value) => {
    return `${value}ms`;
  }
  
  return (
    <>
    
    



    <Card className="admin-panel" variant="outlined">
      <CardContent>

        <Grid container spacing={2}>
          <Grid item xs={4}>
            <FormControl>
              <FormLabel id="demo-radio-buttons-group-label">Grid</FormLabel>
              <FormControlLabel onChange={handleShowGridChange} control={<Checkbox defaultChecked />} label="Show grid" />
              <br/>
   
              <FormLabel id="demo-radio-buttons-group-label">Placeholder</FormLabel>
              <FormControlLabel onChange={handleShowPlaceholders} control={<Checkbox defaultChecked />} label="Show placeholders" />              
              <RadioGroup className="flex-row" defaultValue="nearest" name="radio-buttons-group" onChange={handelPlaceholderTypeChange}>
                <FormControlLabel value="matrix" control={<Radio />} label="Matrix" />
                <FormControlLabel value="nearest" control={<Radio />} label="Nearest" />
              </RadioGroup>
            
              
            </FormControl>
          </Grid>

          <Grid item xs={2}>
              <FormLabel id="demo-radio-buttons-group-label">Update speed</FormLabel>
              <Slider aria-label="Temperature" defaultValue={1000} getAriaValueText={valuetext} valueLabelDisplay="auto" step={100} marks min={100} max={1500}/>

              <FormLabel id="demo-radio-buttons-group-label">Move speed</FormLabel>
              <Slider aria-label="Temperature" defaultValue={200} getAriaValueText={valuetext} valueLabelDisplay="auto" step={10} marks min={10} max={400}/>
          </Grid>

          <Grid item xs={2}>
            <FormLabel id="demo-radio-buttons-group-label">Grid</FormLabel>
            <br/>
            <FormControlLabel onChange={handleShowGridChange} control={<Checkbox defaultChecked />} label="Show grid" />
            <MinimumDistanceSlider/>
          </Grid>
          
        </Grid>

        {/* <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
          Word of the Day
        </Typography> */}
       
        {/* <CardActions>
          <Button size="small">Learn More</Button>
        </CardActions> */}
      </CardContent>
    </Card>


    <MapContainer
      doubleClickZoom={false}
      preferCanvas={true}
      id="mapId"
      zoom={14}
      center={[52.5200, 13.4050]} 
    >
      <TileLayer 
      
        // url="http://localhost:5000/tile/v1/car/tile({x},{y},{z}).mvt"
        // url="http://localhost:8080/tile/v1/car/tile({x},{y},{z})"
        url="http://localhost:9001/tile/{z}/{x}/{y}.png"
        // url="https://server.arcgisonline.com/ArcGIS/rest/services/Canvas/World_Light_Gray_Base/MapServer/tile/{z}/{y}/{x}"
        attribution="Tiles &copy; Esri &mdash; Sources: GEBCO, NOAA, CHS, OSU, UNH, CSUMB, National Geographic, DeLorme, NAVTEQ, and Esri"
        
      />

      {showPlaceholder && (<PlaceholderLayer placeholderType={placeholderType}></PlaceholderLayer>)}
      {showGrid && (<GridLayer onMoveEnd={handleOnMoveEnd}></GridLayer>)} 
      <Polyline pathOptions={fillBlueOptions} positions={pos} />
      {/* <Person  position={marker}></Person> */}
      {markers}
      {elements}
      
      <RoutineMachine />
    </MapContainer>


    </>
  );
};

export default Mapp;
