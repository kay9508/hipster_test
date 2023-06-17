import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './board.reducer';

export const BoardDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const boardEntity = useAppSelector(state => state.board.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="boardDetailsHeading">
          <Translate contentKey="testApp.board.detail.title">Board</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="testApp.board.id">Id</Translate>
            </span>
          </dt>
          <dd>{boardEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="testApp.board.title">Title</Translate>
            </span>
          </dt>
          <dd>{boardEntity.title}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="testApp.board.content">Content</Translate>
            </span>
          </dt>
          <dd>{boardEntity.content}</dd>
          <dt>
            <span id="delAt">
              <Translate contentKey="testApp.board.delAt">Del At</Translate>
            </span>
          </dt>
          <dd>{boardEntity.delAt ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/board" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/board/${boardEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BoardDetail;
