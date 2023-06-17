import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BoardReComment from './board-re-comment';
import BoardReCommentDetail from './board-re-comment-detail';
import BoardReCommentUpdate from './board-re-comment-update';
import BoardReCommentDeleteDialog from './board-re-comment-delete-dialog';

const BoardReCommentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BoardReComment />} />
    <Route path="new" element={<BoardReCommentUpdate />} />
    <Route path=":id">
      <Route index element={<BoardReCommentDetail />} />
      <Route path="edit" element={<BoardReCommentUpdate />} />
      <Route path="delete" element={<BoardReCommentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BoardReCommentRoutes;
