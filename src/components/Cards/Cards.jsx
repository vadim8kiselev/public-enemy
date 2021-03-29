/* eslint-disable react/prop-types */
import { Typography } from '@material-ui/core';
import React from 'react';
import Block from './Block/Block';
import "./Card.scss"

const Card = props => {
  const { element } = props;

  return (
    <div style={{background:element.color, margin: '10px'}}>
      <Typography variant='h5' component='h2' className="ss-title">{element.title}</Typography>
      <div  className="main">
      {element.widgets.map((item, index) => (
        <Block data={item} key={index}/>
      ))}
      </div>
    </div>
  );
};
export default Card;
