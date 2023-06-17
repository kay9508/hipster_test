import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './board-comment.reducer';

export const BoardCommentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const boardCommentEntity = useAppSelector(state => state.boardComment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="boardCommentDetailsHeading">
          <Translate contentKey="testApp.boardComment.detail.title">BoardComment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="testApp.boardComment.id">Id</Translate>
            </span>
          </dt>
          <dd>{boardCommentEntity.id}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="testApp.boardComment.content">Content</Translate>
            </span>
          </dt>
          <dd>{boardCommentEntity.content}</dd>
          <dt>
            <span id="delAt">
              <Translate contentKey="testApp.boardComment.delAt">Del At</Translate>
            </span>
          </dt>
          <dd>{boardCommentEntity.delAt ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="testApp.boardComment.board">Board</Translate>
          </dt>
          <dd>{boardCommentEntity.board ? boardCommentEntity.board.id : ''}</dd>
          <dt>
            <Translate contentKey="testApp.boardComment.board">Board</Translate>
          </dt>
          <dd>{boardCommentEntity.board ? boardCommentEntity.board.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/board-comment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/board-comment/${boardCommentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BoardCommentDetail;
