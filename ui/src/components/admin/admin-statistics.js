import * as React from "react";
import Paper from "@mui/material/Paper";
import {
  Button,
  Card,
  CardContent,
  Checkbox,
  FormControl,
  FormControlLabel,
  FormLabel,
  Grid,
  Radio,
  RadioGroup,
  Slider,
  TextField,
  Typography,
} from "@mui/material";
import axios from "axios";
import styles from './admin-statistics.module.css';

export default function AdminStatistics(props) {

  const [statistics, setStatistics] = React.useState({});

  React.useEffect(() => {
    const intervalId = setInterval(() => {

      const url = `http://localhost:8080/api/engine/statistics`;
      axios.get(url)
      .then(function (response) {
        setStatistics(response.data)
      })
      .catch(function (error) {
        console.log(error);
      });
 
    }, 1000);

    return () => clearInterval(intervalId); //This is important
  },[])

    return (
      <div className={styles.panel}>
        <Grid container spacing={1}>
          <Grid container item xs={12}>

          <Grid item xs={12}>
            <Typography className={styles.title}>Processing:</Typography>
          </Grid>

            <Grid className={styles.item1} item xs={6}>Processing:</Grid> <Grid className={styles.item2} item xs={6}>{statistics.processingTimeInSecond}ms</Grid>
          

            <Grid item xs={12}>
              <Typography className={styles.title}>Taxi:</Typography>
            </Grid>

            <Grid className={styles.item1} item xs={6}>Total count:</Grid> <Grid className={styles.item2} item xs={6}>{statistics.taxiTotalCount}</Grid>
            <Grid className={styles.item1} item xs={6}>IdleCount:</Grid> <Grid className={styles.item2} item xs={6}>{statistics.taxiIdleCount}</Grid>
            <Grid className={styles.item1} item xs={6}>InProcessCount:</Grid> <Grid className={styles.item2} item xs={6}>{statistics.taxiInProcessCount }</Grid>
            <Grid className={styles.item1} item xs={6}>RouteCount:</Grid> <Grid className={styles.item2} item xs={6}>{statistics.taxiRouteCount}</Grid>
            <Grid className={styles.item1} item xs={6}>RouteLength:</Grid> <Grid className={styles.item2} item xs={6}>{statistics.taxiRouteLength}</Grid>
            <Grid className={styles.item1} item xs={6}>RouteLengthAverage:</Grid> <Grid className={styles.item2} item xs={6}>{statistics.taxiRouteLengthAverage}</Grid>
            <Grid className={styles.item1} item xs={6}>RouteLengthMinimum:</Grid> <Grid className={styles.item2} item xs={6}>{statistics.taxiRouteLengthMinimum}</Grid>
            <Grid className={styles.item1} item xs={6}>RouteLengthMaximum:</Grid> <Grid className={styles.item2} item xs={6}>{statistics.taxiRouteLengthMaximum}</Grid>

            <Grid item xs={12}>
              <Typography className={styles.title}>Passanger:</Typography>
            </Grid>

            <Grid className={styles.item1} item xs={6}>Total count:</Grid> <Grid className={styles.item2} item xs={6}>{statistics.passangerTotalCount}</Grid>
            <Grid className={styles.item1} item xs={6}>IdleCount:</Grid> <Grid className={styles.item2} item xs={6}>{statistics.passangerIdleCount}</Grid>
            <Grid className={styles.item1} item xs={6}>RetryCount:</Grid> <Grid className={styles.item2} item xs={6}>{statistics.passangerRetryCount}</Grid>
            <Grid className={styles.item1} item xs={6}>WaitingCount:</Grid> <Grid className={styles.item2} item xs={6}>{statistics.passangerWaitingCount}</Grid>
            <Grid className={styles.item1} item xs={6}>InProcessCount:</Grid> <Grid className={styles.item2} item xs={6}>{statistics.passangerInProcessCount}</Grid>
            <Grid className={styles.item1} item xs={6}>FinishedCount:</Grid> <Grid className={styles.item2} item xs={6}>{statistics.passangerFinishedCount}</Grid>
            <Grid className={styles.item1} item xs={6}>WaitingTimeCount:</Grid> <Grid className={styles.item2} item xs={6}>{statistics.passangerWaitingTimeCount}</Grid>
            <Grid className={styles.item1} item xs={6}>WaitingTimeTotal:</Grid> <Grid className={styles.item2} item xs={6}>{statistics.passangerWaitingTimeTotal}</Grid>
            <Grid className={styles.item1} item xs={6}>WaitingTimeLongest:</Grid> <Grid className={styles.item2} item xs={6}>{statistics.passagnerWaitingTimeLongest}</Grid>
            <Grid className={styles.item1} item xs={6}>WaitingTimeAverage:</Grid> <Grid className={styles.item2} item xs={6}>{statistics.passagnerWaitingTimeAverage}</Grid>
            <Grid className={styles.item1} item xs={6}>WaitingTimeMinimum:</Grid> <Grid className={styles.item2} item xs={6}>{statistics.passagnerWaitingTimeMinimum}</Grid>


          </Grid>
        </Grid>
      </div>
    );
  }

