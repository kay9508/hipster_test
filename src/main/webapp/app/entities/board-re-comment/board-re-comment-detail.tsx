import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './board-re-comment.reducer';

export const BoardReCommentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const boardReCommentEntity = useAppSelector(state => state.boardReComment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="boardReCommentDetailsHeading">
          <Translate contentKey="testApp.boardReComment.detail.title">BoardReComment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="testApp.boardReComment.id">Id</Translate>
            </span>
          </dt>
          <dd>{boardReCommentEntity.id}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="testApp.boardReComment.content">Content</Translate>
            </span>
          </dt>
          <dd>{boardReCommentEntity.content}</dd>
          <dt>
            <span id="delAt">
              <Translate contentKey="testApp.boardReComment.delAt">Del At</Translate>
            </span>
          </dt>
          <dd>{boardReCommentEntity.delAt ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="testApp.boardReComment.boardComent">Board Coment</Translate>
          </dt>
          <dd>{boardReCommentEntity.boardComent ? boardReCommentEntity.boardComent.id : ''}</dd>
          <dt>
            <Translate contentKey="testApp.boardReComment.boardComment">Board Comment</Translate>
          </dt>
          <dd>{boardReCommentEntity.boardComment ? boardReCommentEntity.boardComment.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/board-re-comment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/board-re-comment/${boardReCommentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BoardReCommentDetail;
