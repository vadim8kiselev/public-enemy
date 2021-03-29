/* eslint-disable react/prop-types */
import React from 'react';
import Typography from '@material-ui/core/Typography';
import './Block.scss';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';

const Block = props => {
  const { data } = props;
  return (
    <>
      <Card className="content">
        <CardHeader title={data.title} className="content__title"/>        
        <Typography component="p" className="content__main-info">
          {data.message}
        </Typography>
      </Card>
    </>
  );
};
export default Block;
