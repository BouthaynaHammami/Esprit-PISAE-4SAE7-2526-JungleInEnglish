export const environment = {
  production: false,
  apiUrl: 'http://localhost:8081',
  devUnityUrl: 'http://localhost:8082',
  employeeUrl: 'http://localhost:8083',
  // Direct microservice WebSocket endpoints for Kanban reminders
  academicWsUrl: 'http://localhost:8081/academics/api/ws-reminders',
  communityWsUrl: 'http://localhost:8081/communities/api/ws-reminders',
  // Social/Chat microservice
  socialApiUrl: 'http://localhost:8081/socials/api/chat',
  socialWsUrl:  'http://localhost:8081/socials/api/ws-chat'
};